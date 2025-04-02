package vo;

public class OrdersDetailVO {
	private int ordersDetailId;
	private int ordersId;
	private int itemId;
	private int orderQty;
	public OrdersDetailVO(int ordersDetailId, int ordersId, int itemId, int orderQty) {
		super();
		this.ordersDetailId = ordersDetailId;
		this.ordersId = ordersId;
		this.itemId = itemId;
		this.orderQty = orderQty;
	}
	public OrdersDetailVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getOrdersDetailId() {
		return ordersDetailId;
	}
	public void setOrdersDetailId(int ordersDetailId) {
		this.ordersDetailId = ordersDetailId;
	}
	public int getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(int ordersId) {
		this.ordersId = ordersId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
}