import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Data {
	static int T;//计划排产天数（排产结束时间-排产开始时间）
	static int keyStageNum;//关键工序个数
	static int wheelsetClassNum;//轮对品号数量
	static int repairClassNum;//检修任务数量
	static int orderNum;//排产订单数量	
	static String []stageName = new String[keyStageNum];//关键工序名称
	static int[][]leadTime = new int[keyStageNum][keyStageNum];//关键工序彼此间的leadtime大小	
	static int []classIdArray = new int[wheelsetClassNum];//存储轮对品号id的数组		
	static int []repairIdArray = new int[repairClassNum];//后面可以用键值对直接查找id在数组中对应的位置	
	static String[][] wheelsetClassInfo = new String[wheelsetClassNum][2];//存储对应品号的 车型/动拖 信息	
	static String[][] repairClassInfo = new String[repairClassNum][5];//存储轮对检修任务对应的 品号/车型/动拖/修程/是否到限 这四类信息	
	static int[][] pathData = new int[repairClassNum][keyStageNum];//和轮对检修任务id相对应的轮对关键工序检修工艺，数组值为0/1
	static int[] procTime = new int[keyStageNum];//工序工时	
	static int [][] calendar = new int[keyStageNum][T];//以分钟为单位，排产期内每一天的工序总可用时间（如同一工序有多台设备，则为累加值）
	static ArrayList<Wheelset> newArrivalHigh = new ArrayList<>();//新送修的高级修轮对列表
	static ArrayList<Wheelset> newArrivalTemp = new ArrayList<>();//新送修的临修轮对列表
	static ArrayList<Wheelset> backList = new ArrayList<>();//委外未送回的轮对列表
	static ArrayList<Wheelset> WIPList = new ArrayList<>();//在制轮对列表
	static Order[] orderList = new Order[Data.orderNum];//用于在算法执行过程中更新订单未满足需求信息		
	static int []initHalfInventory = new int [wheelsetClassNum];//当前时刻半成品库各品号轮对的初始数量
	static int []newWheelset =  new int [wheelsetClassNum];//当前时刻各品号新轮对的数量
}



class Wheelset implements Cloneable{
	String wheelsetId;//轮对号
	int classId;//轮对品号（由车型+动拖决定）
	int repairId;//检修任务编号
	Date arrivalDate;//轮对送修日期
	int trainNumber; //轮对所属车组号
	int carriageNumber;//车厢号
	String repairLevel; //修程
	String type;//轮对动拖属性
	int currPosition;//在制轮对当前所处的关键工序id，非在制轮对无须指定此项
	boolean finished;//是否到限
	boolean outsource = false;//是否为委外未送回轮对,默认是false
	boolean withdral;//是否退轮
	boolean highOrTemp;//为真表示属于高级修轮对，否则为临修
	boolean assembly;//为真表示需要装轴承轴箱，此项主要是考虑部分临修轮对不需要装轴承轴箱
	
	ArrayList<Integer> repairPath = new ArrayList<>();//轮对检修工艺路径，不需要系统传递此项数据，算法根据以上属性自动指定
	
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
	Date dueDate;//订单计划交付时间
	int classId;//订单需求品号
	int amount;//订单需求数量
	int trainNumber; //订单录入时对应的车组号
	int priority;//订单优先级（数量越小表示优先级越高）
	
	
	//对订单按照其优先级、交期两个维度排序
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


