package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	
	//구매할 상품 담기
	public void insertReceipt(int ordersId, int itemId, int orderQty) {
		
		
		String sql = "INSERT INTO orders_detail(orders_detail_id, orders_id, item_id, order_qty) "
					+ "VALUES(?,?,?,?)";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, getNextNo());
			pstmt.setInt(2, ordersId);
			pstmt.setInt(3, itemId);
			pstmt.setInt(4, orderQty);
			pstmt.executeUpdate();
			
			ConnectionProvider.close(conn, pstmt);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
	}
	public void printReceipt(String custName) {
		String sql = "SELECT o.orders_id, c.cust_id, c.cust_name, o.created, o.pay_type, o.card_no " +
				     "FROM orders o JOIN customer c ON o.cust_id = c.cust_id " +
				     "WHERE c.cust_name = ?";
		try (
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
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
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
	}

	// 영수증 출력 - 주문번호 기준
	public void printReceipt(int ordersId) {
		
		String sql = "SELECT c.cust_id, c.cust_name, o.created, o.pay_type, o.card_no FROM orders o " +
				     "JOIN customer c ON o.cust_id = c.cust_id WHERE o.orders_id = ?";
		try (
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setInt(1, ordersId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				printReceiptBody(
					rs.getInt("cust_id"),
					rs.getString("cust_name"),
					rs.getTimestamp("created").toLocalDateTime(),
					rs.getString("pay_type"),
					rs.getString("card_no"),
					ordersId
				);
			}
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
		System.out.println("========================================");
		System.out.println("║            🛒 KOSTACO 🛒             ║");
		System.out.println("========================================");
		System.out.printf("║ %-10s : %-20s ║\n", "고객명", custName);
		System.out.printf("║ %-10s : %-21s ║\n", "결제 날짜", formattedDate);
		System.out.printf("║ %-10s : %-21d ║\n", "주문 번호", ordersId);
		System.out.printf("║ %-10s : %-20s ║\n", "결제 수단", payType);
		System.out.printf("║ %-10s : %-21s ║\n", "카드 번호", cardNo);
		System.out.println("========================================");

		String detailSql = "SELECT i.item_name, i.item_price, od.order_qty, i.item_promo " +
				         "FROM item i JOIN orders_detail od ON i.item_id = od.item_id " +
				         "WHERE od.orders_id = ?";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(detailSql);
			pstmt.setInt(1, ordersId);
			ResultSet rs = pstmt.executeQuery();

			int total = 0; //구매한 모든 상품의 총 가격
			int count = 0; //구매한 모든 상품의 합계

			System.out.printf("║ %-10s %4s %2s %-4s %5s ║\n", "상품명", "단가", "수량", "프로모션", "합계");
			while (rs.next()) {//주문한 상품의 정보
				String itemName = rs.getString("item_name"); //orders_detail에 저장된 상품명
				int price = rs.getInt("item_price");////orders_detail에 저장된 상품 가격
				int qty = rs.getInt("order_qty");//주문한 상품의 수량
				//프로모션 체크 해서 1이면 1+1으로 표현, 아니면 아무것도 안나오게 
				String promo = rs.getString("item_promo") != null ? "1+1" : "";

				//화면에 나올 각 상품별 수량 프로모션 적용하기 위해 별도 변수에 저장
				//프로모션이면 주문한 상품에서 2배 아니면 그대로 저장
				int displayQty = promo.equals("1+1") ? qty * 2 : qty;
				
				//상품별 가격
				int subtotal = price * qty;

				System.out.printf("║ %s\t%d %d %5s %,8d원║\n", itemName, price, displayQty, promo, subtotal);
				total += subtotal; 
				count += displayQty;

			}
			if(custId != 0) {
				total -= total/10;
			}
			
			System.out.println("========================================");
			System.out.printf("║ %-7s :\t\t%3d개/%,6d원  ║\n", "합계/금액", count, total);
			System.out.println("=============================KOSTACO====\n");
			
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("상세 예외발생: " + e.getMessage());
		}
	}
}