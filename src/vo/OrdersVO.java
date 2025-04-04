package vo;

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

	public int getOrdersId() {
		return orders_id;
	}

	public void setOrdersId(int orders_id) {
		this.orders_id = orders_id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public Integer getCustId() {
		return cust_id;
	}

	public void setCustId(Integer cust_id) {
		this.cust_id = cust_id;
	}

	public String getPayType() {
		return pay_type;
	}

	public void setPayType(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getCardNo() {
		return card_no;
	}

	public void setCardNo(String card_no) {
		this.card_no = card_no;
	}
}