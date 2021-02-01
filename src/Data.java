import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//��κ�ϵͳ��Խ�

public class Data {
	static int T=40;//�Ų�����
	static int keyStageNum=10;
	static int wheelsetClassNum=10;//���ֶ���������+�������Ծ���
	static int repairClassNum=44;//�ɳ���+����+�޳�+�Ƿ��޾���
	static int WIPNum=300;
	static int orderNum = 3;
	static String []stageName = new String[keyStageNum];
	static int[][]stageGap = new int[keyStageNum][keyStageNum];//Ŀǰ��gap�Ͳ�Ʒ�����޹أ��������б仯���ɼ����Ʒ���͵��±�
	static int []classIdArray = new int[wheelsetClassNum];
	static ArrayList<Integer> classIdList = new ArrayList<>();
	static int []repairIdArray = new int[repairClassNum];//��������ü�ֵ��ֱ�Ӳ���id�������ж�Ӧ��λ��
	static ArrayList<Integer> repairIdList = new ArrayList<>();
	static String[][] wheelsetClassInfo = new String[wheelsetClassNum][2];//�洢������ֶ������Ϣ
	static String[][] repairClassInfo = new String[repairClassNum][4];//�洢������ֶ��޳̵Ȼ�����Ϣ	
	static int []initHalfInventory = new int [wheelsetClassNum];
	static Wheelset[]WIP = new Wheelset[WIPNum];
	static int[][] pathData = new int[repairClassNum][keyStageNum];//�㷨��ִ�й����в��մ������Լ��޵��ĵ���Ϣ���ֶ�ָ������·��
	static int[] procTime = new int[keyStageNum];
	static int [][] calendar = new int[keyStageNum][T];//�Է���Ϊ��λ�������ܿ���ʱ��
	static int [][] availTime = new int[keyStageNum][T];//�Է���Ϊ��λ,ʵ�ʹ���ĳЩ�������ֻ��һ��ÿ�����ӹ��������ݣ�Ҫôת��Ϊ��׼��ʱ��Ҫô�ڳ����в����������
	static ArrayList<Wheelset> newArrivalWheelset = new ArrayList<>();//�����޵ĸ߼����ֶ���Ϣ
	static Order[] order1 = new Order[orderNum]; //�洢ԭʼ����	
	static Order[] orderList = new Order[Data.orderNum];//�������㷨ִ�й����и��¶���δ����������Ϣ
	
	//��ȡ��������
	static void reading_data(String path, Data data) {
		try {
			File datafile = new File(path);
			if (!datafile.exists())
				System.out.println("file not exist!");
			Workbook wb = WorkbookFactory.create(datafile);
			Sheet keyStageSheet = wb.getSheet("�ؼ�����");
			Sheet gapSheet = wb.getSheet("����leadtime��Ϣ");
			Sheet calendarSheet = wb.getSheet("����������Ϣ");
			Sheet classSheet = wb.getSheet("�ֶ������Ϣ");
			Sheet pathSheet = wb.getSheet("�ֶԼ��޹���");
			Sheet wheelsetSheet = wb.getSheet("�����ֶ���Ϣ");
			Sheet orderSheet = wb.getSheet("������Ϣ");
			Sheet initHalfSheet = wb.getSheet("���Ʒ���ʼ��Ϣ");
			
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
				//���ֶ�ά���Ժ����õ�ʱ�򲻷��㣬�����½�һ������洢�����߼�ֵ��
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
//				temp.repairLevel = r.getCell(8).getStringCellValue();//������repairId��classId�������ʱ����Ҫ�ظ���ȡ6-9��
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
