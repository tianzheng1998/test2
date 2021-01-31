
import java.util.ArrayList;

public class Wheelset implements Cloneable{
	String wheelsetId;
	int classId;//轮对所属产品类别号（由车型+动拖决定）
	int repairId;//修程等对应的类别号
	int trainNumber; //轮对所属车组号
	int carriageNumber;//车厢号
	int axelNumber;//车轴号
	String repairLevel; //修程
	String type;//动拖属性
	boolean finished;//是否到限
	boolean outsource = false;//是否委外,默认是false
	boolean withdral;//是否退轮
	int arrivalTime;//后面可替换为时间类型
	String highOrTemp;//高级修、临修
	String comment;//用于标注是否为半成品库支出轮对
	
	ArrayList<Integer> repairPath = new ArrayList<>();//临修根据是否要装轴承轴箱确定其是否需要总装工段的工序加工
	
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

