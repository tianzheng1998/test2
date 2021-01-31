
import java.util.ArrayList;

public class Wheelset implements Cloneable{
	String wheelsetId;
	int classId;//�ֶ�������Ʒ���ţ��ɳ���+���Ͼ�����
	int repairId;//�޳̵ȶ�Ӧ������
	int trainNumber; //�ֶ����������
	int carriageNumber;//�����
	int axelNumber;//�����
	String repairLevel; //�޳�
	String type;//��������
	boolean finished;//�Ƿ���
	boolean outsource = false;//�Ƿ�ί��,Ĭ����false
	boolean withdral;//�Ƿ�����
	int arrivalTime;//������滻Ϊʱ������
	String highOrTemp;//�߼��ޡ�����
	String comment;//���ڱ�ע�Ƿ�Ϊ���Ʒ��֧���ֶ�
	
	ArrayList<Integer> repairPath = new ArrayList<>();//���޸����Ƿ�Ҫװ�������ȷ�����Ƿ���Ҫ��װ���εĹ���ӹ�
	
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

