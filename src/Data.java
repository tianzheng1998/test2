import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//如何和系统类对接

public class Data {
	static int T=40;//排产天数
	static int keyStageNum=10;
	static int wheelsetClassNum=10;//由轮对所属车型+动拖属性决定
	static int repairClassNum=44;//由车型+动拖+修程+是否到限决定
	static int WIPNum=300;
	static int orderNum = 3;
	static String []stageName = new String[keyStageNum];
	static int[][]stageGap = new int[keyStageNum][keyStageNum];//目前的gap和产品类型无关，后续如有变化，可加入产品类型的下标
	static int []classIdArray = new int[wheelsetClassNum];
	static ArrayList<Integer> classIdList = new ArrayList<>();
	static int []repairIdArray = new int[repairClassNum];//后面可以用键值对直接查找id在数组中对应的位置
	static ArrayList<Integer> repairIdList = new ArrayList<>();
	static String[][] wheelsetClassInfo = new String[wheelsetClassNum][2];//存储具体的轮对类别信息
	static String[][] repairClassInfo = new String[repairClassNum][4];//存储具体的轮对修程等划分信息	
	static int []initHalfInventory = new int [wheelsetClassNum];
	static Wheelset[]WIP = new Wheelset[WIPNum];
	static int[][] pathData = new int[repairClassNum][keyStageNum];//算法在执行过程中参照此数组以及修到哪的信息给轮对指定工艺路径
	static int[] procTime = new int[keyStageNum];
	static int [][] calendar = new int[keyStageNum][T];//以分钟为单位，工序总可用时间
	static int [][] availTime = new int[keyStageNum][T];//以分钟为单位,实际过程某些工序可能只有一个每日最大加工量的数据，要么转化为标准工时，要么在程序中采用最大数量
	static ArrayList<Wheelset> newArrivalWheelset = new ArrayList<>();//新送修的高级修轮对信息
	static Order[] order1 = new Order[orderNum]; //存储原始数据	
	static Order[] orderList = new Order[Data.orderNum];//用于在算法执行过程中更新订单未满足需求信息
	
	//读取输入数据
	static void reading_data(String path, Data data) {
		try {
			File datafile = new File(path);
			if (!datafile.exists())
				System.out.println("file not exist!");
			Workbook wb = WorkbookFactory.create(datafile);
			Sheet keyStageSheet = wb.getSheet("关键工序");
			Sheet gapSheet = wb.getSheet("工序leadtime信息");
			Sheet calendarSheet = wb.getSheet("工序日历信息");
			Sheet classSheet = wb.getSheet("轮对类别信息");
			Sheet pathSheet = wb.getSheet("轮对检修工艺");
			Sheet wheelsetSheet = wb.getSheet("送修轮对信息");
			Sheet orderSheet = wb.getSheet("订单信息");
			Sheet initHalfSheet = wb.getSheet("半成品库初始信息");
			
			//keystage data
			for(int rowNum = 1; rowNum <= Data.keyStageNum; rowNum++) {
				Row r = keyStageSheet.getRow(rowNum);
				Data.stageName[rowNum - 1] = r.getCell(1).getStringCellValue();
				Data.procTime[rowNum - 1] = (int)r.getCell(2).getNumericCellValue();	
			}
			
			//gap data
			for(int rowNum = 2; rowNum <(2+Data.keyStageNum); rowNum++ ) {
				Row r = gapSheet.getRow(rowNum);
				for(int colNum = 2; colNum < (2+Data.keyStageNum); colNum++) {
					Data.stageGap[rowNum - 2][colNum - 2] = (int)r.getCell(colNum).getNumericCellValue();
				}
			}			
			//calendar data
			for(int rowNum = 1; rowNum <=calendarSheet.getLastRowNum(); rowNum++ ) {
				Row r = calendarSheet.getRow(rowNum);
				for(int colNum = 1; colNum < (1+Data.T); colNum++) {
					Data.calendar[rowNum - 1][colNum - 1] = (int)r.getCell(colNum).getNumericCellValue();
					Data.availTime[rowNum - 1][colNum - 1] = Data.calendar[rowNum - 1][colNum - 1];
				}
			}		
			
			//class data
			for(int rowNum = 1; rowNum <= Data.wheelsetClassNum; rowNum++) {
				Row r = classSheet.getRow(rowNum);
				Data.classIdArray[rowNum -1] = (int)r.getCell(0).getNumericCellValue();
				classIdList.add((int)r.getCell(0).getNumericCellValue());
				Data.wheelsetClassInfo[rowNum - 1][0] = r.getCell(1).getStringCellValue();
				Data.wheelsetClassInfo[rowNum - 1][1] = r.getCell(2).getStringCellValue();
			}
			
			//repairPath data
			for(int rowNum = 1; rowNum <= Data.repairClassNum; rowNum++) {
				Row r = pathSheet.getRow(rowNum);
				//这种多维属性后面用的时候不方便，考虑新建一个对象存储，或者键值对
				Data.repairIdArray[rowNum -1 ] = (int)r.getCell(0).getNumericCellValue();
				Data.repairIdList.add((int)r.getCell(0).getNumericCellValue());
				Data.repairClassInfo[rowNum - 1][0] = r.getCell(1).getStringCellValue();
				Data.repairClassInfo[rowNum - 1][1] = r.getCell(2).getStringCellValue();
				Data.repairClassInfo[rowNum - 1][2] = r.getCell(3).getStringCellValue();
				Data.repairClassInfo[rowNum - 1][3] = r.getCell(4).getStringCellValue();
				for(int colNum = 5; colNum < (5+Data.keyStageNum); colNum++) {
					Data.pathData[rowNum - 1][colNum - 5] = (int)r.getCell(colNum).getNumericCellValue();
				}
			}
			
			//wheelset data
			for(int rowNum = 1; rowNum <= wheelsetSheet.getLastRowNum(); rowNum++) {
				Row r = wheelsetSheet.getRow(rowNum);
				Wheelset temp = new Wheelset();
				temp.wheelsetId = r.getCell(0).getStringCellValue();
				temp.classId = (int)r.getCell(1).getNumericCellValue();
				temp.repairId = (int)r.getCell(2).getNumericCellValue();
				temp.trainNumber = (int)r.getCell(3).getNumericCellValue();
				temp.carriageNumber = (int)r.getCell(4).getNumericCellValue();
				temp.axelNumber = (int)r.getCell(5).getNumericCellValue();
//				temp.repairLevel = r.getCell(8).getStringCellValue();//现在有repairId和classId，因此暂时不需要重复读取6-9列
				temp.highOrTemp = r.getCell(10).getStringCellValue();
				temp.arrivalTime = (int)r.getCell(11).getNumericCellValue();
				temp.outsource = (r.getCell(12).getStringCellValue().equals("Y"));
				Data.newArrivalWheelset.add(temp);				
			}
			
			//order data 
			for(int rowNum = 1; rowNum <= Data.orderNum; rowNum++) {
				Row r = orderSheet.getRow(rowNum);
				Order temp = new Order();
				temp.orderId = r.getCell(0).getStringCellValue();
				temp.dueDate = (int)r.getCell(1).getNumericCellValue();
				temp.Priority = (int)r.getCell(2).getNumericCellValue();
				temp.trainNumber = (int)r.getCell(3).getNumericCellValue();
				temp.classId = (int)r.getCell(4).getNumericCellValue();
				temp.demand = (int)r.getCell(7).getNumericCellValue();
				order1[rowNum - 1] = temp;
				orderList[rowNum - 1] = temp;
			}
			
			//Init inventory
			for(int rowNum = 1; rowNum < Data.wheelsetClassNum; rowNum++) {
				Row r = initHalfSheet.getRow(rowNum);
				initHalfInventory[rowNum - 1] = (int)r.getCell(1).getNumericCellValue();
			}
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
