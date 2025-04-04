package dao;

import common.ConnectionProvider;
import vo.ItemVO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ItemDAO {


    public void insertItem(String name, int itemQty, int itemPrice, String itemManuf, String itemPromo) {


        LocalDateTime itemMadeDate = LocalDateTime.now();  // 현재 날짜와 시간
        LocalDateTime itemExpDate = itemMadeDate.plusYears(1);// 예시: 1년 후로 유효기간 설정

        // LocalDateTime을 java.sql.Timestamp로 변환
        Timestamp sqlItemMadeDate = Timestamp.valueOf(itemMadeDate);
        Timestamp sqlItemExpDate = Timestamp.valueOf(itemExpDate);

        String sql = "INSERT INTO item " +
                "(item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,getNextNo());
            pstmt.setString(2,name);
            pstmt.setInt(3,itemQty);
            pstmt.setInt(4, itemPrice);
            pstmt.setString(5, itemManuf);
            pstmt.setString(6, itemPromo);
            pstmt.setTimestamp(7, sqlItemMadeDate);
            pstmt.setTimestamp(8, sqlItemExpDate);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("제품이 정상 등록 되었습니다.");
            } else {
                System.out.println("제품 등록을 실패했습니다.");
            }
            ConnectionProvider.close(conn,pstmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ItemVO findByName(String searchName) {
        ItemVO itemVO = new ItemVO();
        String sql = "select * from item where item_name = ? ";

        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ITEM_ID");
                String  name = rs.getString("ITEM_NAME");
                int qty = rs.getInt("ITEM_QTY");
                int price = rs.getInt("ITEM_PRICE");
                String manuf = rs.getString("ITEM_MANUF");
                String promo = rs.getString("ITEM_PROMO");

                // Timestamp 값 불러오기
                Timestamp madeDate = rs.getTimestamp("item_made_date"); // Timestamp 객체로 반환됨
                Timestamp expDate = rs.getTimestamp("item_exp_date");

                // Timestamp를 LocalDateTime으로 변환 (필요시)
                LocalDateTime itemMadeDate = madeDate.toLocalDateTime();
                LocalDateTime itemExpDate = expDate.toLocalDateTime();

                itemVO.setItemId(id);
                itemVO.setItemName(name);
                itemVO.setItemQty(qty);
                itemVO.setItemPrice(price);
                itemVO.setItemManuf(manuf);
                itemVO.setItemPromo(promo);
                itemVO.setItemMadeDate(itemMadeDate);
                itemVO.setItemExpDate(itemExpDate);

                ConnectionProvider.close(conn, pstmt, rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemVO;
    }



    public ItemVO findById(int searchId) {

        ItemVO itemVO = new ItemVO();
        String sql = "select * from item where item_id = ? ";

        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, searchId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ITEM_ID");
                String  name = rs.getString("ITEM_NAME");
                int qty = rs.getInt("ITEM_QTY");
                int price = rs.getInt("ITEM_PRICE");
                String manuf = rs.getString("ITEM_MANUF");
                String promo = rs.getString("ITEM_PROMO");

                // Timestamp 값 불러오기
                Timestamp madeDate = rs.getTimestamp("item_made_date"); // Timestamp 객체로 반환됨
                Timestamp expDate = rs.getTimestamp("item_exp_date");

                // Timestamp를 LocalDateTime으로 변환 (필요시)
                LocalDateTime itemMadeDate = madeDate.toLocalDateTime();
                LocalDateTime itemExpDate = expDate.toLocalDateTime();

                itemVO.setItemId(id);
                itemVO.setItemName(name);
                itemVO.setItemQty(qty);
                itemVO.setItemPrice(price);
                itemVO.setItemManuf(manuf);
                itemVO.setItemPromo(promo);
                itemVO.setItemMadeDate(itemMadeDate);
                itemVO.setItemExpDate(itemExpDate);

            }
            ConnectionProvider.close(conn, pstmt, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemVO;
    }


    public List<ItemVO> findAllItem(){
       List<ItemVO> lists = new ArrayList<>();
       String  sql = "select * from item";

        try {
            Connection conn = ConnectionProvider.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
            	ItemVO itemVO = new ItemVO(); //반복문 안으로 옮김
                int id = rs.getInt("ITEM_ID");
                String  name = rs.getString("ITEM_NAME");
                int qty = rs.getInt("ITEM_QTY");
                int price = rs.getInt("ITEM_PRICE");
                String manuf = rs.getString("ITEM_MANUF");
                String promo;

                if(rs.getString("ITEM_PROMO") == null) promo = "";
                else promo = "1+1";

                // Timestamp 값 불러오기
                Timestamp madeDate = rs.getTimestamp("item_made_date"); // Timestamp 객체로 반환됨
                Timestamp expDate = rs.getTimestamp("item_exp_date");

                // Timestamp를 LocalDateTime으로 변환 (필요시)
                LocalDateTime itemMadeDate = madeDate.toLocalDateTime();
                LocalDateTime itemExpDate = expDate.toLocalDateTime();

                itemVO.setItemId(id);
                itemVO.setItemName(name);
                itemVO.setItemQty(qty);
                itemVO.setItemPrice(price);
                itemVO.setItemManuf(manuf);
                itemVO.setItemPromo(promo);
                itemVO.setItemMadeDate(itemMadeDate);
                itemVO.setItemExpDate(itemExpDate);

                lists.add(itemVO);

            }
            ConnectionProvider.close(conn, stmt, rs); //반복문 밖으로 이동
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lists;
    }


    public void updateItem(int itemId, int itemQty) {
    	String sql = "UPDATE item SET item_qty = item_qty - ? WHERE item_id = ?";
    	try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, itemQty);
			pstmt.setInt(2, itemId);

			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("예외발생: " + e.getMessage());
		}
    }

    public void updateItem(String item_name, int itemQty) {
        String sql = "update item set item_qty = ? where item_name = ?";
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, itemQty);
            pstmt.setString(2, item_name);
            int i = pstmt.executeUpdate();
            if (i > 0) {

                System.out.println("수정이 완료되었습니다.");
            }else {
                System.out.println("수정을 실패했습니다.");
            }

            ConnectionProvider.close(conn,pstmt);
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());

        }


    }


    public void deleteByName(String deleteName) {
        String sql = "delete from item where item_name = ? ";
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,deleteName);
            int result = pstmt.executeUpdate();
            if (result > 0) {

                System.out.println("제품 삭제를 완료했습니다");

            } else {
                System.out.println("제품 삭제 실패.");
            }
            ConnectionProvider.close(conn, pstmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNextNo() {
        int no = -1;
        String sql = "select nvl(max(item_id),0)+1 from item";
        try {
            Connection conn = ConnectionProvider.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                no = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return no;
    }
}