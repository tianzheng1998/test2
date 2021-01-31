import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class Solution {
	ArrayList<ArrayList<ArrayList<Wheelset>>> X = new ArrayList<>();//the list of wheelset to be processed at stage k in day t;
	ArrayList<ArrayList<Wheelset>> S = new ArrayList<>();// the list of wheelset to satisfy order o's demand;
	int [][]halfInventory = new int[Data.wheelsetClassNum][Data.T];
	ArrayList<ArrayList<Wheelset>> enterHalfList = new ArrayList<>();//���ƻ�
	ArrayList<ArrayList<Wheelset>> outHalfList = new ArrayList<>();//����ƻ�
	ArrayList<ArrayList<Wheelset>> outsourceList = new ArrayList<>();//ί��ƻ�
	Order[] finalOrderStatus = new Order[Data.orderNum];//��Ӧ�㷨����������orderList״̬�����������������
	
	public Solution(Data data){		
		for(int t = 0; t < Data.T; t++) {
			ArrayList<ArrayList<Wheelset>> stage = new ArrayList<>();
			for(int k = 0; k < Data.keyStageNum; k++) {
				ArrayList<Wheelset> r = new ArrayList<>();
				stage.add(r);
			}
			X.add(stage);
			ArrayList<Wheelset> in = new ArrayList<>();
			enterHalfList.add(in);
			ArrayList<Wheelset> out = new ArrayList<>();
			outHalfList.add(out);	
			ArrayList<Wheelset> outsource = new ArrayList<>();
			outsourceList.add(outsource);
		}
		
		for(int r = 0; r < Data.orderNum; r++) {
			ArrayList<Wheelset> w = new ArrayList<>();
			S.add(w);
		}
		
		for(int i = 0; i< Data.wheelsetClassNum; i++) {
			for(int j = 0; j < Data.T; j++) {
				this.halfInventory[i][j] = Data.initHalfInventory[i];
			}
			
		}
		
	}
	
	void writeResult(String path) {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet1 = wb.createSheet("�¶��Ų��ƻ�");
		XSSFSheet sheet2 = wb.createSheet("���Ʒ����仯");
		//���õ�Ԫ���ʽ

		XSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Arial");		
		XSSFCellStyle cs = wb.createCellStyle(); // ��Ԫ�����ݻ��й���
		cs.setWrapText(true);
		cs.setFont(font);	
		
		XSSFCellStyle cs2 = wb.createCellStyle();//�������ĵ�Ԫ�����ʽ
		XSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);
		font2.setFontName("΢���ź�");
		cs2.setFont(font2);	
		cs2.setAlignment(HorizontalAlignment.CENTER);
		cs2.setVerticalAlignment(VerticalAlignment.CENTER);
		//�����1����
		XSSFRow headRow = sheet1.createRow(0);
		//������
		headRow.createCell(0).setCellValue("��������");
		headRow.getCell(0).setCellStyle(cs2);
		for(int t = 0; t < Data.T; t++) {
			headRow.createCell(t+1).setCellValue(t+1);
		}
		//�����Ƽ�ÿ������
		for(int rowNum = 1; rowNum <= Data.keyStageNum; rowNum++) {
			XSSFRow r = sheet1.createRow(rowNum);
			//���õ�һ�е�Ԫ���ʽ
			r.createCell(0).setCellValue(Data.stageName[rowNum - 1]);
			r.getCell(0).setCellStyle(cs2);
			
			
			for(int t = 0; t < Data.T; t++) {
				XSSFCell cell = r.createCell(t+1);
				cell.setCellStyle(cs);	
				ArrayList<String> result = new ArrayList<>();
					for(int i = 0; i < this.X.get(t).get(rowNum - 1).size(); i++) {
						if(this.X.get(t).get(rowNum - 1).get(i).wheelsetId !=null) {
							String singleContent = this.X.get(t).get(rowNum - 1).get(i).wheelsetId + ":" 
									+ this.X.get(t).get(rowNum - 1).get(i).trainNumber + "-"
									+this.X.get(t).get(rowNum - 1).get(i).carriageNumber + "-"
									+this.X.get(t).get(rowNum - 1).get(i).axelNumber;
							result.add(singleContent);
						}						
					}		
					if(result.size() > 0) {
						String content = String.join("\n", result);
						cell.setCellValue(content);
					}
			}
		}
		int colNum = headRow.getPhysicalNumberOfCells();
		sheet1.setColumnWidth(0, 6000);
		for(int c = 0; c < colNum; c++) {
			sheet1.autoSizeColumn(c);
		}
		
		//�����2����
		XSSFRow headRow2 = sheet2.createRow(0);
		//������
		headRow2.createCell(0).setCellValue("Ʒ��");
		headRow2.getCell(0).setCellStyle(cs2);
		for(int t = 0; t < Data.T; t++) {
			headRow2.createCell(t+1).setCellValue(t+1);
		}
		//�����Ƽ�ÿ������
		for(int rowNum = 1; rowNum <= Data.wheelsetClassNum; rowNum++) {
			XSSFRow r = sheet2.createRow(rowNum);
			//���õ�һ�е�Ԫ���ʽ
			r.createCell(0).setCellValue(Data.classIdArray[rowNum - 1]);
			r.getCell(0).setCellStyle(cs2);
			
			
			for(int t = 0; t < Data.T; t++) {
				XSSFCell cell = r.createCell(t+1);
				cell.setCellStyle(cs);	
				cell.setCellValue(halfInventory[rowNum -1][t]);
			}
		}
		//��3
		XSSFSheet sheet3 = wb.createSheet("ÿ��������");
		XSSFRow headRow3 = sheet3.createRow(0);
		//������
		headRow3.createCell(0).setCellValue("����");
		headRow3.getCell(0).setCellStyle(cs2);
		for(int t = 0; t < Data.T; t++) {
			headRow3.createCell(t+1).setCellValue(t+1);
		}
		//�����Ƽ�ÿ������	
		XSSFRow r = sheet3.createRow(1);
		r.createCell(0).setCellValue("������");
			for(int t = 0; t < Data.T; t++) {
				XSSFCell cell = r.createCell(t+1);
				cell.setCellStyle(cs);	
				ArrayList<String> result = new ArrayList<>();
					for(int i = 0; i < this.enterHalfList.get(t).size(); i++) {
						if(this.enterHalfList.get(t).get(i).wheelsetId !=null) {
							String singleContent = this.enterHalfList.get(t).get(i).wheelsetId + ":" 
									+ this.enterHalfList.get(t).get(i).trainNumber + "-"
									+this.enterHalfList.get(t).get(i).carriageNumber + "-"
									+this.enterHalfList.get(t).get(i).axelNumber;
							result.add(singleContent);
						}						
					}		
					if(result.size() > 0) {
						String content = String.join("\n", result);
						cell.setCellValue(content);
					}
			}
			colNum = headRow3.getPhysicalNumberOfCells();
			for(int c = 0; c < colNum; c++) {
				sheet3.autoSizeColumn(c);
			}
			
			//��4
			XSSFSheet sheet4 = wb.createSheet("ÿ�ճ������");
			XSSFRow headRow4 = sheet4.createRow(0);
			//������
			headRow4.createCell(0).setCellValue("����");
			headRow4.getCell(0).setCellStyle(cs2);
			for(int t = 0; t < Data.T; t++) {
				headRow4.createCell(t+1).setCellValue(t+1);
			}
			//�����Ƽ�ÿ������	
			r = sheet4.createRow(1);
			r.createCell(0).setCellValue("�������");
				for(int t = 0; t < Data.T; t++) {
					XSSFCell cell = r.createCell(t+1);
					cell.setCellStyle(cs);	
					ArrayList<String> result = new ArrayList<>();
						for(int i = 0; i < this.outHalfList.get(t).size(); i++) {
							if(this.outHalfList.get(t).get(i).wheelsetId !=null) {
								String singleContent = this.outHalfList.get(t).get(i).wheelsetId;
								result.add(singleContent);
							}						
						}		
						if(result.size() > 0) {
							String content = String.join("\n", result);
							cell.setCellValue(content);
						}
				}
		
		
		
		try {
			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			System.out.println("Result write successfully!");
		}catch(IOException e) {
			System.out.println("failed in writing! Check whether the xls file is closed or not!");
		}finally {
			try {
				wb.close();
			} catch (IOException e) {
				// TODO
			}
		}		
		
		
	}

}
