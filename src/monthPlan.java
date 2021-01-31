import java.util.ArrayList;
import java.util.Arrays;

public class monthPlan {

	public static void main(String[] args) {
		Data data = new Data();
		String path = "data/输入数据.xlsx";
		data.reading_data(path, data);
		System.out.println("Data input successfully!");
		ArrayList<ArrayList<Wheelset>> remainList = new ArrayList<>();
		ArrayList<ArrayList<Wheelset>> outsourceList = new ArrayList<>();// 记录每天的委外信息，后面还需要合并之后再委外
		Solution solution = new Solution(data);

		for (int t = 0; t < Data.T; t++) {
			ArrayList<Wheelset> r = new ArrayList<>();
			ArrayList<Wheelset> y = new ArrayList<>();
			remainList.add(r);
			outsourceList.add(y);
		}
		for (int i = 0; i < Data.newArrivalWheelset.size(); i++) {
			remainList.get(Data.newArrivalWheelset.get(i).arrivalTime -1).add(Data.newArrivalWheelset.get(i));
		}

//排高级修
		for (int t = 0; t < Data.T; t++) {
			int size = remainList.get(t).size();
			for (int i = 0; i < size; i++) {
				Wheelset currentWheelset = remainList.get(t).get(i).clone();
				int repairIndexPos = Data.repairIdList.indexOf(currentWheelset.repairId);
				int classIndexPos = Data.classIdList.indexOf(currentWheelset.classId);
				// step 1.
				// 先排无须退压轮的,在所有此类型轮对中，具体先修哪辆车？现在还没提现出来，考虑基于交期或者订单优先级决定,，轮对信息考虑给出对应的orderid？如果能给，则将order数组改为orderlist
				if (Data.pathData[repairIndexPos][1] == 0) {
					// 给当前轮对添加工艺，从临修到交验
					// 注意休息时间不能抵消间隔时间，每条轮对排到某一天的时候需要检查一下当天的可用时间是否够用
					for (int j = 0; j < Data.keyStageNum; j++) {
						if (Data.pathData[repairIndexPos][j] == 1)
							currentWheelset.repairPath.add(j);
					}
					int earlistTime = t;//记录下一道工序的最早可加工时间
					int finishedStage = 0;
					int gap = 0;
					int c = 0;
					boolean finished = false;
					int timetoHalf = 0;// 记录到达半成品库的时间
					for (c = 0; c < (currentWheelset.repairPath.size() - 1); c++) {
						gap = Data.stageGap[currentWheelset.repairPath.get(c)][currentWheelset.repairPath.get(c + 1)];
						for (int t2 = earlistTime; t2 < Data.T; t2++) {
							if (Data.availTime[currentWheelset.repairPath
									.get(c)][t2] >= Data.procTime[currentWheelset.repairPath.get(c)]) {
								solution.X.get(t2).get(currentWheelset.repairPath.get(c)).add(currentWheelset);
								Data.availTime[currentWheelset.repairPath
										.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
								earlistTime += gap;
								finishedStage = c;
								if (Data.stageName[currentWheelset.repairPath.get(c)].equals("检修"))
									timetoHalf = t2;
								break;
							}
						}

					}
					// 最后交期的stage单列出来
					if (finishedStage == (currentWheelset.repairPath.size() - 2)) {
						for (int t2 = earlistTime; t2 < Data.T; t2++) {
							if (Data.availTime[currentWheelset.repairPath
									.get(finishedStage + 1)][t2] >= Data.procTime[currentWheelset.repairPath
											.get(finishedStage + 1)]) {
								solution.X.get(t2).get(currentWheelset.repairPath.get(finishedStage + 1))
										.add(currentWheelset);
								Data.availTime[currentWheelset.repairPath
										.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
								finished = true;
								earlistTime = t2;
								break;
							}
						}
					}

					if (finished) {
						// update order
						// demand（在满足交期且车组号相同的情况下才能update，不满足的话则只能修到半成品库并更新半成品库信息，同时释放半成品库之后的时间） and
						// solution，
						// 匹配需求的时候这一步只跟车组号相同的匹配，如果车组号不一样，那么也是修到半成品库并释放总装工段的资源，未满足的需求在step5再处理
						boolean matched = false;
						for (int r = 0; r < Data.orderNum; r++) {
							if ((Data.orderList[r].trainNumber == currentWheelset.trainNumber)
									&& (Data.orderList[r].classId == currentWheelset.classId)
									&& (Data.orderList[r].demand > 0) && (earlistTime <= Data.orderList[r].dueDate -1 )) {
								Data.orderList[r].demand -= 1;
								solution.S.get(r).add(currentWheelset);
								matched = true;
								break;
							}
						}

						if (!matched) {
							// 按类别更新半成品库数量
							for (int i1 = timetoHalf; i1 < Data.T; i1++) {
								solution.halfInventory[classIndexPos][i1] += 1;
							}
							solution.enterHalfList.get(timetoHalf).add(currentWheelset);
							// 释放总装工段的产能,对应齿轮对相关工位的solution.X也需要抹去
							int debug = currentWheelset.repairPath.indexOf(6);

							// 检修工序的index后面可以采取list的形式获取
							for (int k = (1 + currentWheelset.repairPath.indexOf(6)); k < currentWheelset.repairPath
									.size(); k++) {
								for (int t3 = 0; t3 < Data.T; t3++) {
									if (solution.X.get(t3).get(currentWheelset.repairPath.get(k)).indexOf(currentWheelset) >= 0) {
										solution.X.get(t3).get(currentWheelset.repairPath.get(k)).remove(currentWheelset);
										Data.availTime[currentWheelset.repairPath.get(k)][t3] += Data.procTime[currentWheelset.repairPath.get(k)];
										break;
									}
								}
							}
						}

					}
				}

					// step 2. 接着排需要退压轮但不需要委外的
					else if (Data.pathData[repairIndexPos][1] == 1 && Data.repairClassInfo[repairIndexPos][3].equals("T")) {
						// 给当前轮对添加工艺，从临修到交验
						// 注意休息时间不能抵消间隔时间，每条轮对排到某一天的时候需要检查一下当天的可用时间是否够用
						for (int j = 0; j < Data.keyStageNum; j++) {
							if (Data.pathData[repairIndexPos][j] == 1)
								currentWheelset.repairPath.add(j);
						}
						int earlistTime = t;
						int finishedStage = 0;
						int gap = 0;
						int c = 0;
						boolean finished = false;
						int timetoHalf = 0;// 记录到达半成品库的时间
						for (c = 0; c < (currentWheelset.repairPath.size() - 1); c++) {
							gap = Data.stageGap[currentWheelset.repairPath.get(c)][currentWheelset.repairPath.get(c + 1)];
							for (int t2 = earlistTime; t2 < Data.T; t2++) {
								if (Data.availTime[currentWheelset.repairPath
										.get(c)][t2] >= Data.procTime[currentWheelset.repairPath.get(c)]) {
									solution.X.get(t2).get(currentWheelset.repairPath.get(c)).add(currentWheelset);
									Data.availTime[currentWheelset.repairPath
											.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
									earlistTime += gap;
									finishedStage = c;
									if (Data.stageName[currentWheelset.repairPath.get(c)].equals("检修"))
										timetoHalf = t2;
									break;
								}
							}

						}
						// 最后交期的stage单列出来
						if (finishedStage == (currentWheelset.repairPath.size() - 2)) {
							for (int t2 = earlistTime; t2 < Data.T; t2++) {
								if (Data.availTime[currentWheelset.repairPath
										.get(finishedStage + 1)][t2] >= Data.procTime[currentWheelset.repairPath
												.get(finishedStage + 1)]) {
									solution.X.get(t2).get(currentWheelset.repairPath.get(finishedStage + 1))
											.add(currentWheelset);
									Data.availTime[currentWheelset.repairPath
											.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
									finished = true;
									earlistTime = t2;
									break;
								}
							}
						}

						if (finished) {
							// update order
							// demand（在满足交期且车组号相同的情况下才能update，不满足的话则只能修到半成品库并更新半成品库信息，同时释放半成品库之后的时间） and
							// solution，
							// 匹配需求的时候这一步只跟车组号相同的匹配，如果车组号不一样，那么也是修到半成品库并释放总装工段的资源，未满足的需求在step5再处理
							boolean matched = false;
							for (int r = 0; r < Data.orderNum; r++) {
								if ((Data.orderList[r].trainNumber == currentWheelset.trainNumber)
										&& (Data.orderList[r].classId == currentWheelset.classId)
										&& (Data.orderList[r].demand > 0) && (earlistTime <= Data.orderList[r].dueDate)) {
									Data.orderList[r].demand -= 1;
									solution.S.get(r).add(currentWheelset);
									matched = true;
									break;
								}
							}

							if (!matched) {
								// 按类别更新半成品库数量
								for (int i1 = timetoHalf; i1 < Data.T; i1++) {
									solution.halfInventory[classIndexPos][i1] += 1;
								}
								solution.enterHalfList.get(timetoHalf).add(currentWheelset);
								// 释放总装工段的产能,对应齿轮对相关工位的solution.X也需要抹去

								// 检修工序的index后面可以采取list的形式获取
								for (int k = (1 + currentWheelset.repairPath.indexOf(6)); k < currentWheelset.repairPath
										.size(); k++) {
									for (int t3 = timetoHalf; t3 < Data.T; t3++) {
										if (solution.X.get(t3).get(currentWheelset.repairPath.get(k)).indexOf(currentWheelset) >= 0) {
											solution.X.get(t3).get(currentWheelset.repairPath.get(k)).remove(currentWheelset);
											Data.availTime[currentWheelset.repairPath.get(k)][t3] += Data.procTime[currentWheelset.repairPath.get(k)];
											break;
										}
									}
								}
							}

						}
					}
					// step 3. 排需要退轮&委外的
					else if (Data.pathData[repairIndexPos][1] == 1 && Data.repairClassInfo[repairIndexPos][3].equals("M")) {
						//添加工艺信息，到退轮结束
						currentWheelset.repairPath.add(0);
						currentWheelset.repairPath.add(1);
						int earlistTime = t;
						int finishedStage = 0;
						int gap = 0;
						int c = 0;
						boolean finished = false;
						for (c = 0; c < (currentWheelset.repairPath.size() - 1); c++) {
							gap = Data.stageGap[currentWheelset.repairPath.get(c)][currentWheelset.repairPath.get(c + 1)];
							for (int t2 = earlistTime; t2 < Data.T; t2++) {
								if (Data.availTime[currentWheelset.repairPath
										.get(c)][t2] >= Data.procTime[currentWheelset.repairPath.get(c)]) {
									solution.X.get(t2).get(currentWheelset.repairPath.get(c)).add(currentWheelset);
									Data.availTime[currentWheelset.repairPath
											.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
									earlistTime += gap;
									finishedStage = c;
									break;
								}
							}
						}
						
						// 退轮时间
						if (finishedStage == (currentWheelset.repairPath.size() - 2)) {
							for (int t2 = earlistTime; t2 < Data.T; t2++) {
								if (Data.availTime[currentWheelset.repairPath
										.get(finishedStage + 1)][t2] >= Data.procTime[currentWheelset.repairPath
												.get(finishedStage + 1)]) {
									currentWheelset.outsource = true;
									solution.X.get(t2).get(currentWheelset.repairPath.get(finishedStage + 1))
											.add(currentWheelset);
									Data.availTime[currentWheelset.repairPath
											.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
									finished = true;
									earlistTime = t2;
									solution.outsourceList.get(earlistTime).add(currentWheelset);	
									break;
								}
							}
						}
							
					}
					//step 4. 排委外送回的,主要是考虑到所有新送修轮对中不需要预修工序的那部分均为委外送回,工艺从压轮开始往后直接到半成品库
					else if (currentWheelset.outsource) {
						// 给当前轮对添加工艺，从压轮到半成品库
						// 注意休息时间不能抵消间隔时间，每条轮对排到某一天的时候需要检查一下当天的可用时间是否够用
						for (int j = 4; j < 7; j++) {
							if (Data.pathData[repairIndexPos][j] == 1)
								currentWheelset.repairPath.add(j);
						}
						int earlistTime = t;//记录下一道工序的最早可加工时间
						int finishedStage = 0;
						int gap = 0;
						int c = 0;
						boolean finished = false;
						for (c = 0; c < (currentWheelset.repairPath.size() - 1); c++) {
							gap = Data.stageGap[currentWheelset.repairPath.get(c)][currentWheelset.repairPath.get(c + 1)];
							for (int t2 = earlistTime; t2 < Data.T; t2++) {
								if (Data.availTime[currentWheelset.repairPath
										.get(c)][t2] >= Data.procTime[currentWheelset.repairPath.get(c)]) {
									solution.X.get(t2).get(currentWheelset.repairPath.get(c)).add(currentWheelset);
									Data.availTime[currentWheelset.repairPath
											.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
									earlistTime += gap;
									finishedStage = c;
									break;
								}
							}

						}
						
						
						// 最后交期的stage单列出来
						if (finishedStage == (currentWheelset.repairPath.size() - 2)) {
							for (int t2 = earlistTime; t2 < Data.T; t2++) {
								if (Data.availTime[currentWheelset.repairPath
										.get(finishedStage + 1)][t2] >= Data.procTime[currentWheelset.repairPath
												.get(finishedStage + 1)]) {
									solution.X.get(t2).get(currentWheelset.repairPath.get(finishedStage + 1))
											.add(currentWheelset);
									Data.availTime[currentWheelset.repairPath
											.get(c)][t2] -= Data.procTime[currentWheelset.repairPath.get(c)];
									finished = true;
									earlistTime = t2;
									solution.enterHalfList.get(earlistTime).add(currentWheelset);
									// 按类别更新半成品库数量
									for (int i1 = earlistTime; i1 < Data.T; i1++) {
										solution.halfInventory[classIndexPos][i1] += 1;
									}
									break;
								}
							}
						}

					} 
					else 
						System.out.println("Unexpected Wheelset");
			}


		}
		// step 5.基于前四部得到的半成品库更新后的状态排半成品库的出库计划，此时针对的主要是前面几步未能按期满足交付的订单需求,出库计划根据订单交期倒推
		// 注意指定路径前要先把所有的半成品库轮对的路径清空，月度计划无法控制轮径差这一因素，因此不予考虑。
		//依据订单交期、订单优先级对订单进行排序
		Arrays.sort(Data.orderList);
		for(int r = 0; r < Data.orderNum; r++) {
			if(Data.orderList[r].demand > 0) {
				int dueDay = Data.orderList[r].dueDate;
				int classPos = Data.classIdList.indexOf(Data.orderList[r].classId);
				//总装工段的工艺只和产品类型有关   和修程等无关  
				//如果是全新轮对则不需要上述过程（目前暂不考虑，否则需要在算法中追踪半成品库每条轮对的状态，并确定支出哪条轮对，如果数量很少，则其多占用的产能可忽略）
				//根据交期倒排半成品库出库计划
				int gap1 = 1;//从整备支出到压轴承/联轴节组装，如果部分不需要这部分工序，后面多加个判断，更改对应产品的gap，或者直接在输入上加一个总装工段和产品相关的gap二维时间数组
				int gap2 = 1;//从压轴承到组装交验
				for(int t = dueDay - 1; t >=0; t--) {
					
					if(t>0) {
						//判断产能即半成品库存量是否足够支出
						int maxAmount = Math.min((int)(Data.availTime[8][t]/Data.procTime[8]), Data.orderList[r].demand);//the maximum amount that can be inserted in day t, 以压轴承为瓶颈，其它都是辅助工序不再考虑产能约束
						maxAmount = Math.min(maxAmount, solution.halfInventory[classPos][t]);
						//更新解（此时插入的轮对没有确切的id等信息，只标注一下该轮对是从半成品库支出的，如果后面要追踪到单条则不存在此问题）
						if(maxAmount > 0) {
							Wheelset outWheelset = new Wheelset();
							//标注出库：
							outWheelset.classId = Data.orderList[r].classId;
							outWheelset.wheelsetId = outWheelset.classId + "类型出库"+maxAmount+"条";							
							outWheelset.comment = "半成品库支出轮对";
							solution.X.get(t).get(8).add(outWheelset);
							solution.X.get(t+1).get(9).add(outWheelset);
							solution.X.get(Math.max(t-1, 0)).get(7).add(outWheelset);
							solution.outHalfList.get(Math.max(t-1, 0)).add(outWheelset);
							//更新半成品库库存信息
							for (int i1 = t; i1 < Data.T; i1++) {
								solution.halfInventory[classPos][i1] -= maxAmount;
							}
							
							//更新可用产能信息
							Data.availTime[8][t] = Data.availTime[8][t] - maxAmount*Data.procTime[8];
							//更新订单未满足需求
							Data.orderList[r].demand -= maxAmount;
						}

					}else {
						//在实操过程中不应当出现在t=-1的时候才整备支出的情况，但这里还是先加一个判断并给出预警，提醒要么延长交付期限，要么使用新轮对
						//给出预警：考虑加一个预警轮对list？
						//TODO
					}
					if(Data.orderList[r].demand == 0) {
						break;
					}
					
				}
			}
		}
		
		
//2.排临修订单，基本思路同高级修，初始状态基于1的结果
//TODO
	

//3. 最后调整，首先一定check一下订单交期满足情况，check一下某些工序的数量、负荷等，进行合批，如果某些天检修的数量过少，则需要调整关键工序的gap时间
//另外如果待修库里面还有一些轮对，则把这些轮对用于调节负荷情况安排投产
//TODO
		


//4. 结果输出：可依据orderList最后的状态给出订单的满足情况：哪些订单未满足，未满足量是多少	
	String outPath = "data/" + "月度计划输出结果.xlsx";
	solution.writeResult(outPath);	
	}
}
