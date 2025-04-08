package vo;

public class CustomerVO {

	//고객아이디
    private int custId;
    //고객이름.
    private String custName;
    //고객주민번호
    private String  custBirth;
    //고객 주소
    private String custAddr;
    //고객연락처
    private String custPhone;
    
    //고객 구매금액 조회를 위한 custSalePrice추가  - 04/03
    //고객 데이터 분석및 구매금액 합계를 산출하여 주요 고객 및 VIP고객 데이터 산출 유도하기 위한 변수.
    private int custSalePrice;

    public String getCustAddr() {
        return custAddr;
    }

    public void setCustAddr(String custAddr) {
        this.custAddr = custAddr;
    }

    public String getCustBirth() {
        return custBirth;
    }

    public void setCustBirth(String custBirth) {
        this.custBirth = custBirth;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public CustomerVO() {
    }
    
    public int getCustSalePrice() {
		return custSalePrice;
	}

	public void setCustSalePrice(int custSalePrice) {
		this.custSalePrice = custSalePrice;
	}
	
	
	//CustomerVO 생성자 1
	public CustomerVO(int custId, String custName, String custBirth, String custAddr, String custPhone) {
        this.custAddr = custAddr;
        this.custBirth = custBirth;
        this.custId = custId;
        this.custName = custName;
        this.custPhone = custPhone;
    }
    
	//CustomerVO 생성자 2(구매금액 합계변수까지 포함.)
    public CustomerVO(int custId, String custName, String custBirth, String custAddr, String custPhone, int custPrice) {
        this.custAddr = custAddr;
        this.custBirth = custBirth;
        this.custId = custId;
        this.custName = custName;
        this.custPhone = custPhone;
        this.custSalePrice = custPrice;
    }
}