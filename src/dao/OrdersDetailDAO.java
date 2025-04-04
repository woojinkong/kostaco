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

				if(custId > 0 ) ordersPrice = (int) ((ordersQty * rs.getInt("item_price")) * (0.9));
				else ordersPrice = ordersQty * rs.getInt("item_price");
				
				if("1+1".equals(rs.getString("item_promo"))){ 
					ordersQty *= 2;
					promo = "1+1";
				}
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
