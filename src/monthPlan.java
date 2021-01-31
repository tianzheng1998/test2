import java.util.ArrayList;
import java.util.Arrays;

public class monthPlan {

	public static void main(String[] args) {
		Data data = new Data();
		String path = "data/��������.xlsx";
		data.reading_data(path, data);
		System.out.println("Data input successfully!");
		ArrayList<ArrayList<Wheelset>> remainList = new ArrayList<>();
		ArrayList<ArrayList<Wheelset>> outsourceList = new ArrayList<>();// ��¼ÿ���ί����Ϣ�����滹��Ҫ�ϲ�֮����ί��
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

//�Ÿ߼���
		for (int t = 0; t < Data.T; t++) {
			int size = remainList.get(t).size();
			for (int i = 0; i < size; i++) {
				Wheelset currentWheelset = remainList.get(t).get(i).clone();
				int repairIndexPos = Data.repairIdList.indexOf(currentWheelset.repairId);
				int classIndexPos = Data.classIdList.indexOf(currentWheelset.classId);
				// step 1.
				// ����������ѹ�ֵ�,�����д������ֶ��У��������������������ڻ�û���ֳ��������ǻ��ڽ��ڻ��߶������ȼ�����,���ֶ���Ϣ���Ǹ�����Ӧ��orderid������ܸ�����order�����Ϊorderlist
				if (Data.pathData[repairIndexPos][1] == 0) {
					// ����ǰ�ֶ���ӹ��գ������޵�����
					// ע����Ϣʱ�䲻�ܵ������ʱ�䣬ÿ���ֶ��ŵ�ĳһ���ʱ����Ҫ���һ�µ���Ŀ���ʱ���Ƿ���
					for (int j = 0; j < Data.keyStageNum; j++) {
						if (Data.pathData[repairIndexPos][j] == 1)
							currentWheelset.repairPath.add(j);
					}
					int earlistTime = t;//��¼��һ�����������ɼӹ�ʱ��
					int finishedStage = 0;
					int gap = 0;
					int c = 0;
					boolean finished = false;
					int timetoHalf = 0;// ��¼������Ʒ���ʱ��
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
								if (Data.stageName[currentWheelset.repairPath.get(c)].equals("����"))
									timetoHalf = t2;
								break;
							}
						}

					}
					// ����ڵ�stage���г���
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
						// demand�������㽻���ҳ������ͬ������²���update��������Ļ���ֻ���޵����Ʒ�Ⲣ���°��Ʒ����Ϣ��ͬʱ�ͷŰ��Ʒ��֮���ʱ�䣩 and
						// solution��
						// ƥ�������ʱ����һ��ֻ���������ͬ��ƥ�䣬�������Ų�һ������ôҲ���޵����Ʒ�Ⲣ�ͷ���װ���ε���Դ��δ�����������step5�ٴ���
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
							// �������°��Ʒ������
							for (int i1 = timetoHalf; i1 < Data.T; i1++) {
								solution.halfInventory[classIndexPos][i1] += 1;
							}
							solution.enterHalfList.get(timetoHalf).add(currentWheelset);
							// �ͷ���װ���εĲ���,��Ӧ���ֶ���ع�λ��solution.XҲ��ҪĨȥ
							int debug = currentWheelset.repairPath.indexOf(6);

							// ���޹����index������Բ�ȡlist����ʽ��ȡ
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

					// step 2. ��������Ҫ��ѹ�ֵ�����Ҫί���
					else if (Data.pathData[repairIndexPos][1] == 1 && Data.repairClassInfo[repairIndexPos][3].equals("T")) {
						// ����ǰ�ֶ���ӹ��գ������޵�����
						// ע����Ϣʱ�䲻�ܵ������ʱ�䣬ÿ���ֶ��ŵ�ĳһ���ʱ����Ҫ���һ�µ���Ŀ���ʱ���Ƿ���
						for (int j = 0; j < Data.keyStageNum; j++) {
							if (Data.pathData[repairIndexPos][j] == 1)
								currentWheelset.repairPath.add(j);
						}
						int earlistTime = t;
						int finishedStage = 0;
						int gap = 0;
						int c = 0;
						boolean finished = false;
						int timetoHalf = 0;// ��¼������Ʒ���ʱ��
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
									if (Data.stageName[currentWheelset.repairPath.get(c)].equals("����"))
										timetoHalf = t2;
									break;
								}
							}

						}
						// ����ڵ�stage���г���
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
							// demand�������㽻���ҳ������ͬ������²���update��������Ļ���ֻ���޵����Ʒ�Ⲣ���°��Ʒ����Ϣ��ͬʱ�ͷŰ��Ʒ��֮���ʱ�䣩 and
							// solution��
							// ƥ�������ʱ����һ��ֻ���������ͬ��ƥ�䣬�������Ų�һ������ôҲ���޵����Ʒ�Ⲣ�ͷ���װ���ε���Դ��δ�����������step5�ٴ���
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
								// �������°��Ʒ������
								for (int i1 = timetoHalf; i1 < Data.T; i1++) {
									solution.halfInventory[classIndexPos][i1] += 1;
								}
								solution.enterHalfList.get(timetoHalf).add(currentWheelset);
								// �ͷ���װ���εĲ���,��Ӧ���ֶ���ع�λ��solution.XҲ��ҪĨȥ

								// ���޹����index������Բ�ȡlist����ʽ��ȡ
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
					// step 3. ����Ҫ����&ί���
					else if (Data.pathData[repairIndexPos][1] == 1 && Data.repairClassInfo[repairIndexPos][3].equals("M")) {
						//��ӹ�����Ϣ�������ֽ���
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
						
						// ����ʱ��
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
					//step 4. ��ί���ͻص�,��Ҫ�ǿ��ǵ������������ֶ��в���ҪԤ�޹�����ǲ��־�Ϊί���ͻ�,���մ�ѹ�ֿ�ʼ����ֱ�ӵ����Ʒ��
					else if (currentWheelset.outsource) {
						// ����ǰ�ֶ���ӹ��գ���ѹ�ֵ����Ʒ��
						// ע����Ϣʱ�䲻�ܵ������ʱ�䣬ÿ���ֶ��ŵ�ĳһ���ʱ����Ҫ���һ�µ���Ŀ���ʱ���Ƿ���
						for (int j = 4; j < 7; j++) {
							if (Data.pathData[repairIndexPos][j] == 1)
								currentWheelset.repairPath.add(j);
						}
						int earlistTime = t;//��¼��һ�����������ɼӹ�ʱ��
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
						
						
						// ����ڵ�stage���г���
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
									// �������°��Ʒ������
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
		// step 5.����ǰ�Ĳ��õ��İ��Ʒ����º��״̬�Ű��Ʒ��ĳ���ƻ�����ʱ��Ե���Ҫ��ǰ�漸��δ�ܰ������㽻���Ķ�������,����ƻ����ݶ������ڵ���
		// ע��ָ��·��ǰҪ�Ȱ����еİ��Ʒ���ֶԵ�·����գ��¶ȼƻ��޷������־�����һ���أ���˲��迼�ǡ�
		//���ݶ������ڡ��������ȼ��Զ�����������
		Arrays.sort(Data.orderList);
		for(int r = 0; r < Data.orderNum; r++) {
			if(Data.orderList[r].demand > 0) {
				int dueDay = Data.orderList[r].dueDate;
				int classPos = Data.classIdList.indexOf(Data.orderList[r].classId);
				//��װ���εĹ���ֻ�Ͳ�Ʒ�����й�   ���޳̵��޹�  
				//�����ȫ���ֶ�����Ҫ�������̣�Ŀǰ�ݲ����ǣ�������Ҫ���㷨��׷�ٰ��Ʒ��ÿ���ֶԵ�״̬����ȷ��֧�������ֶԣ�����������٣������ռ�õĲ��ܿɺ��ԣ�
				//���ݽ��ڵ��Ű��Ʒ�����ƻ�
				int gap1 = 1;//������֧����ѹ���/�������װ��������ֲ���Ҫ�ⲿ�ֹ��򣬺����Ӹ��жϣ����Ķ�Ӧ��Ʒ��gap������ֱ���������ϼ�һ����װ���κͲ�Ʒ��ص�gap��άʱ������
				int gap2 = 1;//��ѹ��е���װ����
				for(int t = dueDay - 1; t >=0; t--) {
					
					if(t>0) {
						//�жϲ��ܼ����Ʒ������Ƿ��㹻֧��
						int maxAmount = Math.min((int)(Data.availTime[8][t]/Data.procTime[8]), Data.orderList[r].demand);//the maximum amount that can be inserted in day t, ��ѹ���Ϊƿ�����������Ǹ��������ٿ��ǲ���Լ��
						maxAmount = Math.min(maxAmount, solution.halfInventory[classPos][t]);
						//���½⣨��ʱ������ֶ�û��ȷ�е�id����Ϣ��ֻ��עһ�¸��ֶ��ǴӰ��Ʒ��֧���ģ��������Ҫ׷�ٵ������򲻴��ڴ����⣩
						if(maxAmount > 0) {
							Wheelset outWheelset = new Wheelset();
							//��ע���⣺
							outWheelset.classId = Data.orderList[r].classId;
							outWheelset.wheelsetId = outWheelset.classId + "���ͳ���"+maxAmount+"��";							
							outWheelset.comment = "���Ʒ��֧���ֶ�";
							solution.X.get(t).get(8).add(outWheelset);
							solution.X.get(t+1).get(9).add(outWheelset);
							solution.X.get(Math.max(t-1, 0)).get(7).add(outWheelset);
							solution.outHalfList.get(Math.max(t-1, 0)).add(outWheelset);
							//���°��Ʒ������Ϣ
							for (int i1 = t; i1 < Data.T; i1++) {
								solution.halfInventory[classPos][i1] -= maxAmount;
							}
							
							//���¿��ò�����Ϣ
							Data.availTime[8][t] = Data.availTime[8][t] - maxAmount*Data.procTime[8];
							//���¶���δ��������
							Data.orderList[r].demand -= maxAmount;
						}

					}else {
						//��ʵ�ٹ����в�Ӧ��������t=-1��ʱ�������֧��������������ﻹ���ȼ�һ���жϲ�����Ԥ��������Ҫô�ӳ��������ޣ�Ҫôʹ�����ֶ�
						//����Ԥ�������Ǽ�һ��Ԥ���ֶ�list��
						//TODO
					}
					if(Data.orderList[r].demand == 0) {
						break;
					}
					
				}
			}
		}
		
		
//2.�����޶���������˼·ͬ�߼��ޣ���ʼ״̬����1�Ľ��
//TODO
	

//3. ������������һ��checkһ�¶����������������checkһ��ĳЩ��������������ɵȣ����к��������ĳЩ����޵��������٣�����Ҫ�����ؼ������gapʱ��
//����������޿����滹��һЩ�ֶԣ������Щ�ֶ����ڵ��ڸ����������Ͷ��
//TODO
		


//4. ��������������orderList����״̬���������������������Щ����δ���㣬δ�������Ƕ���	
	String outPath = "data/" + "�¶ȼƻ�������.xlsx";
	solution.writeResult(outPath);	
	}
}
