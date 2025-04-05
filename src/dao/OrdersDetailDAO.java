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
	public void insertReceipt(int ordersId, int itemId, String ordersName, int ordersQty, String ordersPromo, int ordersPrice) {
		String sql = "INSERT INTO orders_detail(orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) "
				+ "VALUES(?,?,?,?,?,?,?)";
		try {

			
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, getNextNo());
			pstmt.setInt(2, ordersId);
			pstmt.setInt(3, itemId);
			pstmt.setString(4, ordersName);
			pstmt.setInt(5, ordersQty);
			pstmt.setString(6, ordersPromo);
			pstmt.setInt(7, ordersPrice);
			pstmt.executeUpdate();
			
			ConnectionProvider.close(conn, pstmt);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
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
}
