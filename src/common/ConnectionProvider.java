package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionProvider {
    public static String driver = "oracle.jdbc.driver.OracleDriver";
    public static String url = "jdbc:oracle:thin:@localhost:1521:XE";
    public static String username = "c##kostaco";
    public static String password = "kostaco";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return conn;

    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
    }

    public static void close(Connection conn, Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
    }
    

}