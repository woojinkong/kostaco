package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import vo.OrdersVO;

import common.ConnectionProvider;

public class OrdersDAO {
	public int getNextNo() {
		int no = -1;
		String sql = "SELECT NVL(MAX(orders_id),0) + 1 FROM orders";
		
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				no = rs.getInt(1);
			}

			ConnectionProvider.close(conn, stmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
		return no;
	}
	
	//주문 날짜 형식 변경하여 반환
	public String getCreated(int ordersId) {
		LocalDateTime created =null;
		String orderCreated = "";
		String sql = "SELECT created AS created FROM orders WHERE orders_id = ?";
		
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ordersId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				created = rs.getTimestamp("created").toLocalDateTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				orderCreated = created.format(formatter);
				
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
				
		return orderCreated;
	}
	public int getOrdersId() {
		String sql = "SELECT MAX(orders_id) FROM orders";
		int re = -1;
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if(rs.next()) re = rs.getInt(1);
			ConnectionProvider.close(conn, stmt);
			
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
		return re;
	}

	// 주문 등록
	public int insertOrders (int custId, String cardNo, String payType) {
		String sql = "insert into orders (orders_id, created, cust_id, card_no, pay_type) values(?,?,?,?,?)";
		try (Connection conn = ConnectionProvider.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)){ 
			// gpt왈: try-catch 말고 try-with-resources 로 자원누수방지 finally/close()문을 사용하지 않고 앞에서 해결!
			int nextNo = getNextNo();
			LocalDateTime nowDate = LocalDateTime.now(); 
			java.sql.Timestamp sqlnowDate = java.sql.Timestamp.valueOf(nowDate);
			pstmt.setInt(1, nextNo);
			pstmt.setTimestamp(2, sqlnowDate);
		
			pstmt.setInt(3, custId);
			pstmt.setString(4, cardNo); 
			pstmt.setString(5, payType);
			
			// gpt왈: 주문등록 성공한 경우 삽입된 행 갯수 반환 
			pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
			return nextNo;
		} catch (Exception e) {
			System.out.println("예외발생:주문등록"+e.getMessage());
			return 0;
		}
	}
	
	// 주문 삭제
	public int deleteOrders (int orders_id) {
		String sql = "delete from orders where orders_id=?";
		
		try (Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
						
			pstmt.setInt(1, orders_id);
			int rowsAffected = pstmt.executeUpdate();
			
			if(rowsAffected > 0) {
				System.out.println(orders_id + "번 주문이 삭제되었습니다.");
			}else {
				System.out.println(orders_id + "번 주문을 찾을 수 없습니다.");
			}
			
			return rowsAffected;
			
		} catch (Exception e) {
			System.out.println("예외발생:주문삭제 "+e.getMessage());
			return 0; //예외 발생 시 0 반환
		}
		
	}
	
	// 주문 전체 검색
	public List<OrdersVO> findAllOrders () {
		List<OrdersVO> ordersList = new ArrayList<>();
		String sql = "select orders_id, created, cust_id, card_no, pay_type from orders";
		
		try (Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()){
					
					while (rs.next()) {
					int order_id = rs.getInt("orders_id");
					LocalDateTime created = rs.getTimestamp("created").toLocalDateTime();
					Integer cust_id = rs.getObject("cust_id") != null ? rs.getInt("cust_id") : null;
					String card_no = rs.getString("card_no");
					String pay_type = rs.getString("pay_type");
					
					// OrderVO 객체 생성, 리스트에 추가
					OrdersVO orders = new OrdersVO(order_id, created, cust_id, pay_type, card_no);
					ordersList.add(orders);
				}
			
		} catch (Exception e) {
			System.out.println("예외발생:주문전체 "+e.getMessage());
		}
		return ordersList; 
	}
	
	// 주문 고객아이디로 검색
	public List<OrdersVO> findOrdersByCustId (int cust_id) {
		List<OrdersVO> ordersList = new ArrayList<>();
		String sql = "select orders_id, created, cust_id, card_no, pay_type from orders where cust_id = ?";
		
		try (Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
					
			pstmt.setInt(1, cust_id);
			
			try (ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					int orders_id = rs.getInt("orders_id");
					LocalDateTime created = rs.getTimestamp("created").toLocalDateTime();
					String card_no = rs.getString("card_no");
					String pay_type	= rs.getString("pay_type");
					
					OrdersVO orders = new OrdersVO(orders_id, created, cust_id, card_no, pay_type);
					ordersList.add(orders);
				}
			}
			
		} catch (SQLException e) {
			System.out.println("예외발생:주문고객id"+e.getMessage());
		}
		return ordersList;
	}
	
	// 주문 고객아이디로 검색
	public List<OrdersVO> findOrdersByCustIdAndCreated (int cust_id, String created) {
		List<OrdersVO> ordersList = new ArrayList<>();
		String sql = "select orders_id, created, cust_id, card_no, pay_type from orders where cust_id = ? and to_char(created, 'yyyy/mm/dd') = ?";
		
		try (Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
					
			pstmt.setInt(1, cust_id);
			pstmt.setString(2, created);
			try (ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					int orders_id = rs.getInt("orders_id");
					LocalDateTime orderCreated = rs.getTimestamp("created").toLocalDateTime();
					String card_no = rs.getString("card_no");
					String pay_type	= rs.getString("pay_type");
					
					OrdersVO orders = new OrdersVO(orders_id, orderCreated, cust_id, card_no, pay_type);
					ordersList.add(orders);
				}
			}
			
		} catch (SQLException e) {
			System.out.println("예외발생:주문고객id"+e.getMessage());
		}
		return ordersList;
	}
	
	// OrdersDAO.java
	public OrdersVO findOrderById(int ordersId) {
	    OrdersVO vo = new OrdersVO();
	    String sql = "SELECT orders_id, cust_id, pay_type, card_no, created FROM orders WHERE orders_id = ?";
	    try (
	        Connection conn = ConnectionProvider.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql)
	    ) {
	        pstmt.setInt(1, ordersId);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            vo.setOrdersId(rs.getInt("orders_id"));
	            vo.setCustId(rs.getInt("cust_id"));
	            vo.setPayType(rs.getString("pay_type"));
	            vo.setCardNo(rs.getString("card_no"));
	            vo.setCreated(rs.getTimestamp("created").toLocalDateTime());
	        }
	        rs.close();
	    } catch (Exception e) {
	        System.out.println("주문 정보 조회 오류: " + e.getMessage());
	    }
	    return vo;
	}

}