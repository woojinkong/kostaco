package vo;

import java.time.LocalDateTime;
import java.util.Date;

public class CustomerVO {

    private int custId;
    private String custName;
    private String  custBirth;
    private String custAddr;
    private String custPhone;
    
    //고객 구매금액 조회를 위한 custSalePrice추가  - 04/03
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

	public CustomerVO(int custId, String custName, String custBirth, String custAddr, String custPhone) {
        this.custAddr = custAddr;
        this.custBirth = custBirth;
        this.custId = custId;
        this.custName = custName;
        this.custPhone = custPhone;
    }
    
    public CustomerVO(int custId, String custName, String custBirth, String custAddr, String custPhone, int custPrice) {
        this.custAddr = custAddr;
        this.custBirth = custBirth;
        this.custId = custId;
        this.custName = custName;
        this.custPhone = custPhone;
        this.custSalePrice = custPrice;
    }
}