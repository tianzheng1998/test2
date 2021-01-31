
public class Order implements Comparable<Order>{
	String orderId;
	int dueDate;
	int trainNumber; //轮对所属车组号
	int Priority;//数值越小表示订单优先级越高
	int classId;
	int demand;
	//临修有其额外的配置需求，数量很少，暂时不放进来，先把高级修处理完成再说
	
	//对订单按照其优先级、交期两个维度排序
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
