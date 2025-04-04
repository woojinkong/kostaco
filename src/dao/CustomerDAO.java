package dao;

import common.ConnectionProvider;
import vo.CustomerVO;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    //고객 이름과 핸드폰 뒷자리번호 4개로 고객번호 찾기 예(이름 공우진, 번호 5948)
    public int findCustByNameAndPhone(String custName, String custPhone) {
        int custId = -1;
        //고객이름과 고객 핸드폰번호 끝에서부터 4자리의 숫자를 비쇼하여 고객 아이디를 반환합니다.
        String sql = "select cust_id from customer where cust_name = ? "
                + "and substr(cust_phone, length(cust_phone) - 3, 4) = ?";
        Connection conn =  ConnectionProvider.getConnection();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, custName);
            prst.setString(2, custPhone);
            ResultSet rs = prst.executeQuery();
            if(rs.next()) {
                System.out.println("고객 조회 성공!");
                custId = rs.getInt(1);
            }else {
                System.out.println("고객 조회 실패!");
            }
            ConnectionProvider.close(conn, prst,rs);

        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return custId;
    }

    //특정 날자에 N번째 손님의 고객번호를 찾는 메소드
    public int findCustIdByDateAndN(String date, int N) {
        int custId = 0;

        String sql = "select r, cust_id "
                + "from (select rownum r, cust_id "
                + "from orders "
                + "where created = ?) "
                + "where r = ?";
        Connection conn = ConnectionProvider.getConnection();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, date);
            prst.setInt(2, N);
            ResultSet rs = prst.executeQuery();
            if(rs.next()) {
                System.out.println("고객 조회 성공!");
                custId = rs.getInt(1);
            }else {
                System.out.println("고객 조회 실패!");
            }
        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return custId;
    }


    //고객번호로 회원정보 가져오기
    public CustomerVO getCustByCustid(int custId) {
        CustomerVO custVo = null;

        String sql = "select * from customer where cust_id = ?";
        Connection conn =  ConnectionProvider.getConnection();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setInt(1, custId);

            ResultSet rs = prst.executeQuery();
            if(rs.next()) {
                custVo = new CustomerVO(rs.getInt(1),rs.getString(2),
                        rs.getString(3),rs.getString(4),rs.getString(5));
            }

            ConnectionProvider.close(conn, prst, rs);

        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }

        return custVo;
    }


    //전체 고객 조회 후 리스트에 담는 메소드
    public ArrayList<CustomerVO> findAllCust(){
        ArrayList<CustomerVO> list = new ArrayList<CustomerVO>();
        String sql = "select * from customer";
        Connection conn = ConnectionProvider.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                list.add(new CustomerVO(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5))
                );
            }

            ConnectionProvider.close(conn, stmt,rs);
        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return list;
    }



    //회원등록  고객번호, 고객이름, 고객생일, 고객주소, 고객핸드폰번호
    public int insertCustomer(String cust_name,
                              String cust_birth, String cust_addr, String cust_phone ) {
        int re = -1;
        int index = getCustomerLastIndex();
        String sql = "insert into customer values(?,?,?,?,?)";
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, index);
            pstmt.setString(2, cust_name);
            pstmt.setString(3, cust_birth);
            pstmt.setString(4, cust_addr);
            pstmt.setString(5, cust_phone);
            re = pstmt.executeUpdate();

            ConnectionProvider.close(conn, pstmt);
        } catch (Exception e) {
            System.out.println("예외 발생 : " + e.getMessage());
        }
        return re;
    }


    //고객번호로 회원삭제
    public int deleteCustomer(int cust_id) {
        int re = -1;
        String sql = "delete from customer where cust_id = ?";
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cust_id);
            re = pstmt.executeUpdate();
            ConnectionProvider.close(conn, pstmt);
        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return re;
    }

    //시퀀스 대신 넣은 고객번호 다음값을 가져오는 메소드.
    public int getCustomerLastIndex() {
        int index = -1;
        String sql = "select nvl(max(cust_id),0) from customer";
        Connection conn = ConnectionProvider.getConnection();
        try {
            Statement stmt =  conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                index = rs.getInt(1);
            }
            ConnectionProvider.close(conn, stmt,rs);
        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return index + 1;
    }
    
    
    //특정기간동안 구매금액이 가장 큰 고객 리스트를 원하는 수만큼 가져오는 메소드
    //date1~ date2 기간사이 구매금액이 가장 큰 고객을 count만큼 구하는 메소드
    //예 : 2024/05/12 부터 2025/12/12 까지 매출순으로 고객 5명을 가져온다.
    //가져오는 정보 = 고객번호, 고객매출
    
    public ArrayList<CustomerVO> findVipCustomer(String date1, String date2, int custcount) {
    	ArrayList<CustomerVO> list = new ArrayList<CustomerVO>();
    	
    	String sql = "select * "
    			+ "from (select c.cust_id, sum(orders_price) Sum "
    			+ "from orders_detail od , orders o, customer c "
    			+ "where c.cust_id = o.cust_id and o.orders_id = od.orders_id "
    			+ "and o.created between  TO_DATE(?, 'YYYY/MM/DD') and TO_DATE(?, 'YYYY/MM/DD') "
    			+ "group by c.cust_id "
    			+ "having c.cust_id <> 0 "
    			+ "order by Sum desc) "
    			+ "where rownum <= ?";
    	try {
    		Connection conn = ConnectionProvider.getConnection();
			PreparedStatement prst = conn.prepareStatement(sql);
			prst.setString(1, date1);
			prst.setString(2, date2);
			prst.setInt(3, custcount);
			ResultSet rs = prst.executeQuery();
			while(rs.next()) {
				list.add(new CustomerVO(
						getCustByCustid(rs.getInt(1)).getCustId(),
						getCustByCustid(rs.getInt(1)).getCustName(),
						getCustByCustid(rs.getInt(1)).getCustBirth(),
						getCustByCustid(rs.getInt(1)).getCustAddr(),
						getCustByCustid(rs.getInt(1)).getCustPhone(),
						rs.getInt(2)));	
			}
			ConnectionProvider.close(conn, prst,rs);
		} catch (SQLException e) {
			System.out.println("예외발생 : " + e.getMessage());
		}
    	return list;
    }
    
    
    //고객 취향 분석을 위하여
    //특정 상품을 가장 많이 구입한 고객순을 N번째(count)고객까지 조회하는 메소드
    //고객번호 cust_id 를 반환하여 리스트로 넣는다.
    
    public ArrayList<CustomerVO> findCustByItem(String itemName, int count){
    	ArrayList<CustomerVO> list = new ArrayList<CustomerVO>();
    	String sql = "select cust_id, total "
    			+ "from (select cust_id, sum(orders_qty) total "
    			+ "from orders o, orders_detail d, item i "
    			+ "where o.orders_id = d.orders_id and i.item_id = d.item_id and item_name = ? "
    			+ "group by cust_id "
    			+ "order by total desc) "
    			+ "where rownum <= ?";
    	Connection conn = ConnectionProvider.getConnection();
    	try {
			PreparedStatement prst = conn.prepareStatement(sql);
			prst.setString(1, itemName);
			prst.setInt(2, count);
			ResultSet rs = prst.executeQuery();
			while(rs.next()) {
				list.add(new CustomerVO(
						getCustByCustid(rs.getInt(1)).getCustId(),
						getCustByCustid(rs.getInt(1)).getCustName(),
						getCustByCustid(rs.getInt(1)).getCustBirth(),
						getCustByCustid(rs.getInt(1)).getCustAddr(),
						getCustByCustid(rs.getInt(1)).getCustPhone()
						));	
			}
			ConnectionProvider.close(conn, prst, rs);
		} catch (SQLException e) {
			System.out.println("예외발생 : " + e.getMessage());
		}
    	return list;
    }
    
    //회원 정보수정(연락처 수정)
    public void updateCustPhone(int custId, String phone) {
    	
    	String sql = "update customer set cust_phone = ? where cust_id = ?";
    	
    	try {
    		Connection conn = ConnectionProvider.getConnection();
			PreparedStatement prst =  conn.prepareStatement(sql);
			prst.setInt(1, custId);
			prst.setString(2, phone);
			
			int re =  prst.executeUpdate();
			if(re != -1) {
				System.out.println("고객정보 업데이트 성공!");
			}else {
				System.out.println("고객정보 업데이트 실패!");
			}
			ConnectionProvider.close(conn, prst);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    //회원 정보수정(주소 수정)
    public void updateCustAddr(int custId, String addr) {
    	
    	String sql = "update customer set cust_addr = ? where cust_id = ?;";
    	try {
    		Connection conn = ConnectionProvider.getConnection();
			PreparedStatement prst =  conn.prepareStatement(sql);
			prst.setInt(1, custId);
			prst.setString(2, addr);
			
			int re =  prst.executeUpdate();
			if(re != -1) {
				System.out.println("고객정보 업데이트 성공!");
			}else {
				System.out.println("고객정보 업데이트 실패!");
			}
			ConnectionProvider.close(conn, prst);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    

}