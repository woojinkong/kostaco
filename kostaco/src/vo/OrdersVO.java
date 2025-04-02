package vo;
import java.sql.Date;
import java.time.LocalDateTime;

public class OrdersVO {
	private int orders_id;
	 private LocalDateTime created;
	private Integer cust_id;
	private String pay_type;
	private String card_no;
	
	public OrdersVO(int orders_id, LocalDateTime created, Integer cust_id, String pay_type, String card_no) {
		super();
		this.orders_id = orders_id;
		this.created = created;
		this.cust_id = cust_id;
		this.pay_type = pay_type;
		this.card_no = card_no;
	}

	public OrdersVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getOrders_id() {
		return orders_id;
	}

	public void setOrder_id(int orders_id) {
		this.orders_id = orders_id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public Integer getCust_id() {
		return cust_id;
	}

	public void setCust_id(Integer cust_id) {
		this.cust_id = cust_id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	
	

}