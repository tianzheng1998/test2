
public class Order implements Comparable<Order>{
	String orderId;
	int dueDate;
	int trainNumber; //�ֶ����������
	int Priority;//��ֵԽС��ʾ�������ȼ�Խ��
	int classId;
	int demand;
	//���������������������������٣���ʱ���Ž������ȰѸ߼��޴��������˵
	
	//�Զ������������ȼ�����������ά������
	public int compareTo(Order o){
		Order order2 =  o;
		if(this.dueDate < order2.dueDate)
			return -1;
		else if(this.dueDate == order2.dueDate) {
			if(this.Priority < order2.Priority)
				return -1;
			else
				return 1;
		}			
		else
			return 1;
	}	

}
