package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.ConnectionProvider;
import vo.SalesStatsVO;

// 판매 통계 DAO
public class SalesStatsDAO {

    // 월별 총 판매 실적 조회
    public List<SalesStatsVO> getMonthlySales() {
        List<SalesStatsVO> list = new ArrayList<>();
        String sql = "SELECT TO_CHAR(o.created, 'YYYY-MM') AS month, " +
                     "SUM(od.orders_qty * od.orders_price) AS total_sales " +
                     "FROM orders o " +
                     "JOIN orders_detail od ON o.orders_id = od.orders_id " +
                     "GROUP BY TO_CHAR(o.created, 'YYYY-MM') " +
                     "ORDER BY month";

        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                int totalSales = rs.getInt("total_sales");
                list.add(new SalesStatsVO(month, null, null, null, totalSales, 0));
            }

        } catch (Exception e) {
            System.out.println("월별 판매 실적 조회 오류: " + e.getMessage());
        }

        return list;
    }

    // 연도별 총 판매 실적 조회
    public List<SalesStatsVO> getYearlySales() {
        List<SalesStatsVO> list = new ArrayList<>();
        String sql = "SELECT TO_CHAR(o.created, 'YYYY') AS year, " +
                     "SUM(od.orders_qty * od.orders_price) AS total_sales " +
                     "FROM orders o " +
                     "JOIN orders_detail od ON o.orders_id = od.orders_id " +
                     "GROUP BY TO_CHAR(o.created, 'YYYY') " +
                     "ORDER BY year";

        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String year = rs.getString("year");
                int totalSales = rs.getInt("total_sales");
                list.add(new SalesStatsVO(year, totalSales));
            }

        } catch (Exception e) {
            System.out.println("연도별 판매 실적 조회 오류: " + e.getMessage());
        }

        return list;
    }

    // 제조사별 총 판매 실적 조회
    public List<SalesStatsVO> getSalesStats() {
        List<SalesStatsVO> list = new ArrayList<>();
        String sql = "SELECT i.item_manuf AS manufacturer, " +
                     "SUM(od.orders_qty * od.orders_price) AS total_sales " +
                     "FROM orders o " +
                     "JOIN orders_detail od ON o.orders_id = od.orders_id " +
                     "JOIN item i ON od.item_id = i.item_id " +
                     "GROUP BY i.item_manuf " +
                     "ORDER BY total_sales DESC";

        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String manufacturer = rs.getString("manufacturer");
                int totalSales = rs.getInt("total_sales");
                list.add(new SalesStatsVO(null, null, manufacturer, null, totalSales, 0));
            }

        } catch (Exception e) {
            System.out.println("제조사별 총 판매 실적 조회 오류: " + e.getMessage());
        }

        return list;
    }

    // 제조사별 월별 상품 판매 순위 조회 (Top 3)
    public List<SalesStatsVO> getMakerMonthlyItemRankings() {
        List<SalesStatsVO> list = new ArrayList<>();
        String sql = "SELECT month, manufacturer, item_name, quantity_sold, total_sales, rank FROM (" +
                     "  SELECT TO_CHAR(o.created, 'YYYY-MM') AS month, " +
                     "         i.item_manuf AS manufacturer, " +
                     "         i.item_name, " +
                     "         SUM(od.orders_qty) AS quantity_sold, " +
                     "         SUM(od.orders_qty * od.orders_price) AS total_sales, " +
                     "         RANK() OVER (PARTITION BY TO_CHAR(o.created, 'YYYY-MM'), i.item_manuf " +
                     "                    ORDER BY SUM(od.orders_qty * od.orders_price) DESC) AS rank " +
                     "  FROM orders o " +
                     "  JOIN orders_detail od ON o.orders_id = od.orders_id " +
                     "  JOIN item i ON od.item_id = i.item_id " +
                     "  GROUP BY TO_CHAR(o.created, 'YYYY-MM'), i.item_manuf, i.item_name " +
                     ") " +
                     "WHERE rank <= 3 " +
                     "ORDER BY month, manufacturer, rank";

        try (Connection conn = ConnectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                String manufacturer = rs.getString("manufacturer");
                String itemName = rs.getString("item_name");
                int quantitySold = rs.getInt("quantity_sold");
                int totalSales = rs.getInt("total_sales");
                int rank = rs.getInt("rank");

                SalesStatsVO vo = new SalesStatsVO(month, null, manufacturer, itemName, totalSales, rank);
                vo.setQuantitySold(quantitySold); // quantitySold 필드에 setter 필요
                list.add(vo);
            }

        } catch (Exception e) {
            System.out.println("제조사별 월별 상품 판매 순위 조회 오류: " + e.getMessage());
        }

        return list;
    }
}

