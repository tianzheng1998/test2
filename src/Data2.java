import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Data {
	static int T;//�ƻ��Ų��������Ų�����ʱ��-�Ų���ʼʱ�䣩
	static int keyStageNum;//�ؼ��������
	static int wheelsetClassNum;//�ֶ�Ʒ������
	static int repairClassNum;//������������
	static int orderNum;//�Ų���������	
	static String []stageName = new String[keyStageNum];//�ؼ���������
	static int[][]leadTime = new int[keyStageNum][keyStageNum];//�ؼ�����˴˼��leadtime��С	
	static int []classIdArray = new int[wheelsetClassNum];//�洢�ֶ�Ʒ��id������		
	static int []repairIdArray = new int[repairClassNum];//��������ü�ֵ��ֱ�Ӳ���id�������ж�Ӧ��λ��	
	static String[][] wheelsetClassInfo = new String[wheelsetClassNum][2];//�洢��ӦƷ�ŵ� ����/���� ��Ϣ	
	static String[][] repairClassInfo = new String[repairClassNum][5];//�洢�ֶԼ��������Ӧ�� Ʒ��/����/����/�޳�/�Ƿ��� ��������Ϣ	
	static int[][] pathData = new int[repairClassNum][keyStageNum];//���ֶԼ�������id���Ӧ���ֶԹؼ�������޹��գ�����ֵΪ0/1
	static int[] procTime = new int[keyStageNum];//����ʱ	
	static int [][] calendar = new int[keyStageNum][T];//�Է���Ϊ��λ���Ų�����ÿһ��Ĺ����ܿ���ʱ�䣨��ͬһ�����ж�̨�豸����Ϊ�ۼ�ֵ��
	static ArrayList<Wheelset> newArrivalHigh = new ArrayList<>();//�����޵ĸ߼����ֶ��б�
	static ArrayList<Wheelset> newArrivalTemp = new ArrayList<>();//�����޵������ֶ��б�
	static ArrayList<Wheelset> backList = new ArrayList<>();//ί��δ�ͻص��ֶ��б�
	static ArrayList<Wheelset> WIPList = new ArrayList<>();//�����ֶ��б�
	static Order[] orderList = new Order[Data.orderNum];//�������㷨ִ�й����и��¶���δ����������Ϣ		
	static int []initHalfInventory = new int [wheelsetClassNum];//��ǰʱ�̰��Ʒ���Ʒ���ֶԵĳ�ʼ����
	static int []newWheelset =  new int [wheelsetClassNum];//��ǰʱ�̸�Ʒ�����ֶԵ�����
}



class Wheelset implements Cloneable{
	String wheelsetId;//�ֶԺ�
	int classId;//�ֶ�Ʒ�ţ��ɳ���+���Ͼ�����
	int repairId;//����������
	Date arrivalDate;//�ֶ���������
	int trainNumber; //�ֶ����������
	int carriageNumber;//�����
	String repairLevel; //�޳�
	String type;//�ֶԶ�������
	int currPosition;//�����ֶԵ�ǰ�����Ĺؼ�����id���������ֶ�����ָ������
	boolean finished;//�Ƿ���
	boolean outsource = false;//�Ƿ�Ϊί��δ�ͻ��ֶ�,Ĭ����false
	boolean withdral;//�Ƿ�����
	boolean highOrTemp;//Ϊ���ʾ���ڸ߼����ֶԣ�����Ϊ����
	boolean assembly;//Ϊ���ʾ��Ҫװ������䣬������Ҫ�ǿ��ǲ��������ֶԲ���Ҫװ�������
	
	ArrayList<Integer> repairPath = new ArrayList<>();//�ֶԼ��޹���·��������Ҫϵͳ���ݴ������ݣ��㷨�������������Զ�ָ��
	
	public Wheelset clone()  {
		try {
			return (Wheelset) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

class Order implements Comparable<Order>{
	String orderId;
	Date dueDate;//�����ƻ�����ʱ��
	int classId;//��������Ʒ��
	int amount;//������������
	int trainNumber; //����¼��ʱ��Ӧ�ĳ����
	int priority;//�������ȼ�������ԽС��ʾ���ȼ�Խ�ߣ�
	
	
	//�Զ������������ȼ�����������ά������
	public int compareTo(Order o){
		Order order2 =  o;
		if(this.dueDate.before(order2.dueDate))
			return -1;
		else if(this.dueDate == order2.dueDate) {
			if(this.priority < order2.priority)
				return -1;
			else
				return 1;
		}			
		else
			return 1;
	}	

}


