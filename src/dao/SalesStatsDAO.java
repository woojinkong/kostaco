package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.ConnectionProvider;
import vo.SalesStatsVO;

//판매통계 DAO
public class SalesStatsDAO {
	
	//월별 총 판매 실적 조회
	public List<SalesStatsVO> getMonthlySales() {
		List<SalesStatsVO> list = new ArrayList<>();
		String sql = "select TO_CHAR(o.created, 'YYYY-MM') as month, sum(od.orders_qty * od.orders_price) as total_sales " +
					 "From orders o join orders_detail od on o.orders_id = od.orders_id " +
					 "group by TO_CHAR(o.created, 'YYYY-MM') " +
					 "order by month";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String month = rs.getString("month");
				int totalSales = rs.getInt("total_sales");
				list.add(new SalesStatsVO(month, null, null, totalSales, 0));
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("월별 판매 실적 조회 오류: "+ e.getMessage());
		}
		return list;
	}
	
	// 연도별 총 판매 실적 조회
	public List<SalesStatsVO> getYearlySales() {
		List<SalesStatsVO> list = new ArrayList<>();
		String sql = "select TO_CHAR(o.created, 'YYYY') as year, sum(od.orders_qty * od.orders_price) as total_sales " +
					 "from orders o join orders_detail od " +
					 "on o.orders_id = od.orders_id " +
					 "group by TO_CHAR(o.created, 'YYYY') " +
					 "order by year";
		
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String year = rs.getString("year");
				int totalSales = rs.getInt("total_sales");
				list.add(new SalesStatsVO(year, totalSales));
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("연도별 판매 실적 조회 오류: " + e.getMessage());
		}
		return list;
	}
	
	// 제조사별 월별 총 판매 실적 조회
	public List<SalesStatsVO> getMonthlySalesByManufacturer() {
		List<SalesStatsVO> list = new ArrayList<>();
		String sql = "select TO_CHAR(o.created, 'YYYY-MM') as month, i.item_manuf as manufacturer, sum(od.orders_qty * od.orders_price) as total_sales " +
					 "from orders o join orders_detail od on o.orders_id = od.orders_id " +
					 "join item i on od.item_id = i.item_id " +
					 "group by TO_CHAR(o.created, 'YYYY-MM'), i.item_manuf " +
					 "order by month, manufacturer";
		
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String month = rs.getString("month");
				String manufacturer = rs.getString("manufacturer");
				int totalSales = rs.getInt("total_sales");
				list.add(new SalesStatsVO(month, manufacturer, totalSales));
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("제조사별 월별 판매 실적 조회 오류: "+e.getMessage());
		}
		return list;
	}
	
	// 제조사별 월별 상품 판매 순위
	public List<SalesStatsVO> getTopMonthlySalesByManufacturer(int topN) {
		List<SalesStatsVO> list = new ArrayList<>();
		String sql = "SELECT month, manufacturer, item_name, total_sales " + 
					 "FROM (SELECT TO_CHAR(o.created, 'YYYY-MM') AS month, " +
						         "i.item_manuf AS manufacturer, " +
						         "i.item_name, " +
						         "SUM(od.orders_qty * od.orders_price) AS total_sales, " +
						         "RANK() OVER (PARTITION BY TO_CHAR(o.created, 'YYYY-MM'), i.item_manuf " + 
						         "ORDER BY SUM(od.orders_qty * od.orders_price) DESC) AS rank " +
					 "FROM orders o "+
					 "JOIN orders_detail od ON o.orders_id = od.orders_id " +
					 "JOIN item i ON od.item_id = i.item_id " +
					 "GROUP BY TO_CHAR(o.created, 'YYYY-MM'), i.item_manuf, i.item_name) " +
					 "WHERE rank <= ? " +
					 "ORDER BY month, manufacturer, total_sales DESC";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, topN);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String month = rs.getString("month");
				String manufacturer = rs.getString("manufacturer");
				String itemName = rs.getString("item_name");
				int totalSales = rs.getInt("total_sales");
				list.add(new SalesStatsVO(month, manufacturer, itemName, totalSales, 0));
			}
				rs.close();
				pstmt.close();
			
		} catch (Exception e) {
			System.out.println("제조사별 월별 판매 순위 조회 오류: "+e.getMessage());
		}
		return list;
		
	}
}
