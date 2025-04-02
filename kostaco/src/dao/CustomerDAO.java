package dao;

import common.ConnectionProvider;
import vo.CustomerVO;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    //고객 이름과 핸드폰 뒷자리번호 4개로 고객번호 찾기 예(이름 공우진, 번호 5948)
    public int findCustByNameAndPhone(String custName, String custPhone) {
        int custId = -1;
        //고객이름과 고객 핸드폰번호 끝에서부터 4자리의 숫자를 비쇼하여 고객 아이디를 반환합니다.
        String sql = "select cust_id from customer where cust_name = ? "
                + "and substr(cust_phone, length(cust_phone) - 3, 4) = ?";
        Connection conn =  ConnectionProvider.getConnection();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, custName);
            prst.setString(2, custPhone);
            ResultSet rs = prst.executeQuery();
            if(rs.next()) {
                System.out.println("고객 조회 성공!");
                custId = rs.getInt(1);
            }else {
                System.out.println("고객 조회 실패!");
            }
            ConnectionProvider.close(conn, prst,rs);

        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return custId;
    }

    //특정 날자에 N번째 손님의 고객번호를 찾는 메소드
    public int findCustIdByDateAndN(String date, int N) {
        int custId = 0;

        String sql = "select r, cust_id "
                + "from (select rownum r, cust_id "
                + "from orders "
                + "where created = ?) "
                + "where r = ?";
        Connection conn = ConnectionProvider.getConnection();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, date);
            prst.setInt(2, N);
            ResultSet rs = prst.executeQuery();
            if(rs.next()) {
                System.out.println("고객 조회 성공!");
                custId = rs.getInt(1);
            }else {
                System.out.println("고객 조회 실패!");
            }
        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return custId;
    }


    //고객번호로 회원정보 가져오기
    public CustomerVO getCustByCustid(int custId) {
        CustomerVO custVo = null;

        String sql = "select * from customer where cust_id = ?";
        Connection conn =  ConnectionProvider.getConnection();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setInt(1, custId);

            ResultSet rs = prst.executeQuery();
            if(rs.next()) {
                custVo = new CustomerVO(rs.getInt(1),rs.getString(2),
                        rs.getString(3),rs.getString(4),rs.getString(5));
            }

            ConnectionProvider.close(conn, prst, rs);

        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }

        return custVo;
    }


    //전체 고객 조회 후 리스트에 담는 메소드
    public ArrayList<CustomerVO> findAllCust(){
        ArrayList<CustomerVO> list = new ArrayList<CustomerVO>();
        String sql = "select * from customer";
        Connection conn = ConnectionProvider.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                list.add(new CustomerVO(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5))
                );
            }

            ConnectionProvider.close(conn, stmt,rs);
        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return list;
    }



    //회원등록  고객번호, 고객이름, 고객생일, 고객주소, 고객핸드폰번호
    public int insertCustomer(String cust_name,
                              String cust_birth, String cust_addr, String cust_phone ) {
        int re = -1;
        int index = getCustomerLastIndex();
        String sql = "insert into customer values(?,?,?,?,?)";
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, index);
            pstmt.setString(2, cust_name);
            pstmt.setString(3, cust_birth);
            pstmt.setString(4, cust_addr);
            pstmt.setString(5, cust_phone);
            re = pstmt.executeUpdate();

            ConnectionProvider.close(conn, pstmt);
        } catch (Exception e) {
            System.out.println("예외 발생 : " + e.getMessage());
        }
        return re;
    }


    //고객번호로 회원삭제
    public int deleteCustomer(int cust_id) {
        int re = -1;
        String sql = "delete from customer where cust_id = ?";
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cust_id);
            re = pstmt.executeUpdate();
            ConnectionProvider.close(conn, pstmt);
        } catch (Exception e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return re;
    }

    //시퀀스 대신 넣은 고객번호 다음값을 가져오는 메소드.
    public int getCustomerLastIndex() {
        int index = -1;
        String sql = "select nvl(max(cust_id),0) from customer";
        Connection conn = ConnectionProvider.getConnection();
        try {
            Statement stmt =  conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                index = rs.getInt(1);
            }
            ConnectionProvider.close(conn, stmt,rs);
        } catch (SQLException e) {
            System.out.println("예외발생 : " + e.getMessage());
        }
        return index + 1;
    }

}