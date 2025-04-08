package vo;

public class OrdersDetailVO {
	private int ordersDetailId;
	private int ordersId;
	private int itemId;
	private String ordersItem;
	private int ordersQty;
	private String ordersPromo;
	private int ordersPrice;
	
	
	public OrdersDetailVO(String ordersItem, int ordersQty, String ordersPromo, int ordersPrice) {
		super();
		this.ordersItem = ordersItem;
		this.ordersQty = ordersQty;
		this.ordersPromo = ordersPromo;
		this.ordersPrice = ordersPrice;
	}
	
	public String getOrdersPromo() {
		return ordersPromo;
	}
	public void setOrdersPromo(String ordersPromo) {
		this.ordersPromo = ordersPromo;
	}
	public OrdersDetailVO(int ordersDetailId, int ordersId, int itemId, String ordersItem, int ordersQty, int ordersPrice) {
		super();
		this.ordersDetailId = ordersDetailId;
		this.ordersId = ordersId;
		this.itemId = itemId;
		this.ordersItem = ordersItem;
		this.ordersQty = ordersQty;
		this.ordersPrice = ordersPrice;
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
	public int getOrdersQty() {
		return ordersQty;
	}
	public void setOrderQty(int ordersQty) {
		this.ordersQty = ordersQty;
	}
	public String getOrdersItem() {
		return ordersItem;
	}
	public void setOrdersItem(String ordersItem) {
		this.ordersItem = ordersItem;
	}
	public int getOrdersPrice() {
		return ordersPrice;
	}
	public void setOrdersPrice(int ordersPrice) {
		this.ordersPrice = ordersPrice;
	}
	public void setOrdersQty(int ordersQty) {
		this.ordersQty = ordersQty;
	}
	
}
