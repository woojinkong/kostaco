package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dao.ItemDAO;
import vo.OrdersDetailVO;
import common.ConnectionProvider;

public class OrdersDetailDAO {
	
	//새로운 orders_detail_id의 번호를 발행하여 반환하는 메소드
	public int getNextNo() {
		int no = -1;
		String sql = "SELECT NVL(MAX(orders_detail_id),0) + 1 FROM orders_detail";
		
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				no = rs.getInt(1);
			}
			ConnectionProvider.close(conn, stmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
		return no;
	}
	//orders_id와 item_id로 수량 갖고오기
	public int findByOrdersIdAndItemId(int ordersId, int itemId) {
		String sql = "SELECT orders_qty FROM orders_detail WHERE orders_id = ? AND item_id = ?";
		int re = 0;
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ordersId);
			pstmt.setInt(2, itemId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) re = rs.getInt("orders_qty");  
			
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
		return re;
	}
	//구매할 상품 담기
	public void insertReceipt(int ordersId, int itemId, int ordersQty) {
		String subsql = "SELECT (SELECT cust_id FROM orders WHERE orders_id = ?) AS cust_id, item_name, item_promo, item_price FROM item WHERE item_id = ?";

		try {
			Connection subconn = ConnectionProvider.getConnection();
			PreparedStatement subpstmt = subconn.prepareStatement(subsql);
			
			subpstmt.setInt(1, ordersId);
			subpstmt.setInt(2, itemId);
			
			ResultSet rs = subpstmt.executeQuery();
			String orderName = "";
			String promo = " - ";
			int custId = 0;
			int ordersPrice = 0;
			
			if(rs.next()) {
				custId = rs.getInt("cust_id");
				orderName = rs.getString("item_name");
				
				if("1+1".equals(rs.getString("item_promo"))){ 
					ordersQty *= 2;
					promo = "1+1";
				}
				if(custId > 0 ) ordersPrice = (int) ((ordersQty * rs.getInt("item_price")) * (0.9));
				else ordersPrice = ordersQty * rs.getInt("item_price");
			}
			ConnectionProvider.close(subconn, subpstmt);
		
			String mainsql = "INSERT INTO orders_detail(orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) "
					+ "VALUES(?,?,?,?,?,?,?)";
			
			Connection mainconn = ConnectionProvider.getConnection();
			PreparedStatement mainpstmt = mainconn.prepareStatement(mainsql);
			
			mainpstmt.setInt(1, getNextNo());
			mainpstmt.setInt(2, ordersId);
			mainpstmt.setInt(3, itemId);
			mainpstmt.setString(4, orderName);
			mainpstmt.setInt(5, ordersQty);
			mainpstmt.setString(6, promo);
			mainpstmt.setInt(7, ordersPrice);
			mainpstmt.executeUpdate();
			
			ConnectionProvider.close(mainconn, mainpstmt);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
	}
	
	public void printReceipt(List<OrdersDetailVO> list) {
		int total = 0;
		int count = 0;
		System.out.printf("║ %-10s %4s %-4s %5s ║\n", "상품명", "수량", "프로모션", "합계");
		for(OrdersDetailVO odVO : list) {
			System.out.printf("║ %-10s %4s %-4s %5s ║\n", odVO.getOrdersItem(), odVO.getOrdersQty(), odVO.getOrdersPromo(), odVO.getOrdersPrice());
			total += odVO.getOrdersPrice();
			count += odVO.getOrdersQty();
		}
		
		System.out.printf("║ %-10s %5d %5d ║\n", "합계/금액", count, total);
		System.out.println("===============================================");
	}
	
	public List<OrdersDetailVO> receiptList(int ordersId){
		List<OrdersDetailVO> list = new ArrayList<OrdersDetailVO>();
		
		String sql = "SELECT orders_item, orders_qty, orders_promo, orders_price FROM orders_detail WHERE orders_id = ?";
		
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ordersId);
			ResultSet rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				printReceiptBody(
						rs.getInt("cust_id"),
						rs.getString("cust_name"),
						rs.getTimestamp("created").toLocalDateTime(),
						rs.getString("pay_type"),
						rs.getString("card_no"),
						rs.getInt("orders_id")
					);
				
				OrdersDetailVO odVO = new OrdersDetailVO();
				odVO.setOrdersItem(rs.getString("orders_item"));
				odVO.setOrderQty(rs.getInt("orders_qty"));
				odVO.setOrdersPromo(rs.getString("orders_promo"));
				odVO.setOrdersPrice(rs.getInt("orders_price"));
				list.add(odVO);
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
		return list;
	}

	public void printReceipt(String custName) {
		String sql = "SELECT o.orders_id, c.cust_id, c.cust_name, o.created, o.pay_type, o.card_no " +
				     "FROM orders o JOIN customer c ON o.cust_id = c.cust_id " +
				     "WHERE c.cust_name = ?";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
		
			pstmt.setString(1, custName);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				printReceiptBody(
					rs.getInt("cust_id"),
					rs.getString("cust_name"),
					rs.getTimestamp("created").toLocalDateTime(),
					rs.getString("pay_type"),
					rs.getString("card_no"),
					rs.getInt("orders_id")
				);
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
	}
	// 영수증 출력 - 주문번호, 날짜 기준
	public void printReceipt(String custName, String created) {
		
		String sql = "SELECT o.orders_id, c.cust_id, c.cust_name, o.created, o.pay_type, o.card_no FROM orders o " +
				     "JOIN customer c ON o.cust_id = c.cust_id WHERE c.cust_name = ? and to_char(created, 'yyyy/mm/dd') = ?";
		try (
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setString(1, custName);
			pstmt.setString(2, created);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				printReceiptBody(
					rs.getInt("cust_id"),
					rs.getString("cust_name"),
					rs.getTimestamp("created").toLocalDateTime(),
					rs.getString("pay_type"),
					rs.getString("card_no"),
					rs.getInt("orders_id")
				);
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
	}
	
	// 영수증 공통 출력 부분 메서드로 추출
	private void printReceiptBody(int custId, String custName, LocalDateTime created, String payType, String cardNo, int ordersId) {
		//현금이면 카드번호가 null이므로 카드번호에 0000으로 표시
		if (cardNo == null || cardNo.trim().isEmpty() ||
			    cardNo.equalsIgnoreCase("null") || cardNo.equals("0000")) {
			    cardNo = "0000";
			}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate = created.format(formatter);
		System.out.println("=================================");
		System.out.println("║         🛒 KOSTACO 🛒          ║");
		System.out.println("=================================");
		System.out.printf("║ %-5s : %-20s ║\n", "고객명", custName);
		System.out.printf("║ %-5s : %-20s ║\n", "결제 날짜", formattedDate);
		System.out.printf("║ %-5s : %-20d ║\n", "주문 번호", ordersId);
		System.out.printf("║ %-5s : %-19s ║\n", "결제 수단", payType);
		System.out.printf("║ %-5s : %-20s ║\n", "카드 번호", cardNo);
		System.out.println("=================================");
		
		String detailSql = "SELECT orders_item, orders_qty, orders_promo, orders_price FROM orders_detail WHERE orders_id = ?";;
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(detailSql);
			pstmt.setInt(1, ordersId);
			ResultSet rs = pstmt.executeQuery();
			int total = 0;
			int count = 0;
			System.out.printf("║ %-6s %-4s %-4s %8s ║\n", "상품명", "수량", "프로모션", "합계");
			while (rs.next()) {//주문한 상품의 정보
				String orderItem = rs.getString("orders_item"); //orders_detail에 저장된 상품명
				int price = rs.getInt("orders_price");////orders_detail에 저장된 상품 가격
				int qty = rs.getInt("orders_qty");//주문한 상품의 수량
				String promo = rs.getString("orders_promo");
				total += rs.getInt("orders_price");
				count += rs.getInt("orders_qty");
				System.out.printf("║%-6s %6d %5s %,8d원 ║\n", orderItem, qty, promo, price);
			}
			
			System.out.println("==================================");
			System.out.printf("║ %-10s:    %3d개/%,8d원║\n", "합계/금액", count, total);
			System.out.println("=======================KOSTACO====\n");
			
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("상세 예외발생: " + e.getMessage());
		}
	}
}
