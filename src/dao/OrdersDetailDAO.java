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
			ConnectionProvider.close(conn, pstmt, rs);
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
	
	//구매하지 않은 상품 중에 인기있는 상품 TOP5
	public List<Integer> getPopularItem(int custId) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = ""
			    + "SELECT item_id "
			    + "FROM ( "
			    + "    SELECT item_id, SUM(orders_qty) AS total_qty "
			    + "    FROM orders_detail "
			    + "    WHERE item_id IN ( "
			    + "        SELECT item_id "
			    + "        FROM item "
			    + "        WHERE item_id NOT IN ( "
			    + "            SELECT item_id "
			    + "            FROM orders_detail "
			    + "            WHERE orders_id IN ( "
			    + "                SELECT orders_id "
			    + "                FROM orders "
			    + "                WHERE cust_id = ? "
			    + "            ) "
			    + "        ) "
			    + "    ) "
			    + "    GROUP BY item_id, orders_item "
			    + "    ORDER BY total_qty DESC "
			    + ") "
			    + "WHERE ROWNUM <= 5";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, custId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getInt("item_id"));
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
		return list;
	}
	//고객 번호를 받아 해당 고객이 가장 많이 구매한 물품을 찾고 해당 물품을 구매한 다른 고객들이 같이 구매한 다른 상품을 추천하며 이미 구매한 상품은 추천하지 않는다.
	public List<Integer> getRecommendItem(int custId){
		List<Integer> list = new ArrayList<Integer>();
		String sql = "SELECT distinct od1.item_id "
		        	+ "FROM orders o1 "
		        	+ "JOIN orders_detail od1 ON o1.orders_id = od1.orders_id "
			        + "JOIN item i ON od1.item_id = i.item_id "
			        + "WHERE o1.cust_id IN ( "
			        + "    SELECT o2.cust_id "
			        + "    FROM orders o2 "
			        + "    JOIN orders_detail od2 ON o2.orders_id = od2.orders_id "
			        + "    WHERE od2.item_id = ( "
			        + "        SELECT item_id "
			        + "        FROM ( "
			        + "            SELECT item_id "
			        + "            FROM orders_detail "
			        + "            WHERE orders_id IN ( "
			        + "                SELECT orders_id FROM orders WHERE cust_id = ? "
			        + "            ) "
			        + "            GROUP BY item_id "
			        + "            ORDER BY SUM(orders_qty) DESC "
			        + "        ) "
			        + "        WHERE ROWNUM = 1 "
			        + "    ) "
			        + "    AND o2.cust_id != ? "
			        + ") "
			        + "AND od1.item_id NOT IN ( "
			        + "    SELECT item_id "
			        + "    FROM orders_detail "
			        + "    WHERE orders_id IN ( "
			        + "        SELECT orders_id FROM orders WHERE cust_id = ? "
			        + "    ) "
			        + ")"
			        + "ORDER BY item_id";
		try {
			Connection conn = ConnectionProvider.getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, custId);
			pstmt.setInt(2, custId);
			pstmt.setInt(3, custId);
		
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getInt("item_id"));
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
		return list;
	}
}