package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import common.ConnectionProvider;

public class init {

	public static void init() {
		createUser();
		grant();
		createTable();
		insertData();
	}
	
	
	public static void createUser() {
		try {
			Connection conn = ConnectionProvider.getConnection("system", "manager");
			String sql ="create user c##kostaco identified by kostaco";
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			ConnectionProvider.close(conn, stmt);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
	}
	
	public static void grant() {
		try {
			Connection conn = ConnectionProvider.getConnection("system", "manager");			
			String sql ="grant connect, resource, dba to c##bit";
			PreparedStatement pstmt = conn.prepareStatement(sql);	
			pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
	}
	
	public static void createTable() {
		String[] sqlStatements = {

				"CREATE TABLE customer ( cust_Id NUMBER NOT NULL, cust_name VARCHAR2(20) NOT NULL, cust_birth VARCHAR2(20) NULL, cust_addr VARCHAR2(50) NULL, cust_phone VARCHAR2(20) NOT NULL)",
				"ALTER TABLE customer ADD ( PRIMARY KEY (cust_Id) )",
				"CREATE TABLE item ( item_id NUMBER NOT NULL, item_name VARCHAR2(50) NOT NULL, item_qty NUMBER NOT NULL, item_price NUMBER NOT NULL, item_manuf VARCHAR2(20) NOT NULL, item_promo VARCHAR2(20) NULL, item_made_date DATE NOT NULL, item_exp_date DATE NOT NULL )",
				"ALTER TABLE item ADD ( PRIMARY KEY (item_id) )",
				"CREATE TABLE orders_detail ( orders_detail_id NUMBER NOT NULL, orders_id NUMBER NOT NULL, item_id NUMBER NOT NULL, orders_item VARCHAR2(50) NOT NULL, orders_qty NUMBER NOT NULL, orders_promo VARCHAR2(20) NULL, orders_price NUMBER NOT NULL )",
				"ALTER TABLE orders_detail ADD ( PRIMARY KEY (orders_detail_id) )",
				"CREATE TABLE orders ( orders_id NUMBER NOT NULL, cust_Id NUMBER NULL, card_no VARCHAR2(20) NULL, pay_type VARCHAR(20) NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP )",
				"ALTER TABLE orders ADD ( PRIMARY KEY (orders_id) )",
				"ALTER TABLE orders_detail ADD ( FOREIGN KEY (orders_id) REFERENCES orders )",
				"ALTER TABLE orders_detail ADD ( FOREIGN KEY (item_id) REFERENCES item )",
				"ALTER TABLE orders ADD  ( FOREIGN KEY (cust_Id) REFERENCES customer ON DELETE SET NULL )"

	        };
			
			try {
				Connection conn = ConnectionProvider.getConnection("c##kostaco", "kostaco");
	            Statement stmt = conn.createStatement();

	            for (String sql : sqlStatements) {	                
	                 stmt.executeUpdate(sql);
	                 System.out.println("Executed: " + sql);
	            }
	            ConnectionProvider.close(conn, stmt);
			}catch (Exception e) {
				System.out.println("예외발생:"+e.getMessage());
			}
	    }
	public static void insertData() {
		   
	    String[] insertStatements = {
	        // customer 샘플 데이터
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (0, '비회원', 0, 0, 0)",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (1, '김철수', '1990-05-14', '서울시 강남구', '010-1234-5678')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (2, '이영희', '1985-08-23', '부산시 해운대구', '010-2345-6789')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (3, '박지훈', '1992-12-01', '대구시 수성구', '010-3456-7890')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (4, '최민수', '1988-06-17', '인천시 남동구', '010-4567-8901')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (5, '정하늘', '1995-11-25', '광주시 북구', '010-5678-9012')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (6, '한지우', '2000-01-10', '대전시 서구', '010-6789-0123')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (7, '서윤아', '1993-07-08', '울산시 남구', '010-7890-1234')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (8, '고명석', '1987-03-30', '경기도 성남시', '010-8901-2345')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (9, '장다혜', '1991-09-15', '경기도 수원시', '010-9012-3456')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (10, '윤도현', '1989-04-20', '강원도 춘천시', '010-0123-4567')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (11, '배성훈', '1994-02-14', '충청북도 청주시', '010-1234-5670')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (12, '임혜진', '1986-12-22', '충청남도 천안시', '010-2345-6781')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (13, '조민호', '1997-10-09', '전라북도 전주시', '010-3456-7892')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (14, '송지연', '1998-05-05', '전라남도 광양시', '010-4567-8903')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (15, '문석준', '1990-08-19', '경상북도 포항시', '010-5678-9014')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (16, '김다솔', '1996-03-03', '경상남도 창원시', '010-6789-0125')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (17, '홍세진', '1993-06-12', '제주특별자치도 제주시', '010-7890-1236')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (18, '박윤서', '2001-07-24', '서울시 종로구', '010-8901-2347')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (19, '이준혁', '1992-09-01', '경기도 고양시', '010-9012-3458')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (20, '차은우', '1999-11-30', '부산시 북구', '010-0123-4569')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (21, '권민정', '1984-04-08', '대전시 동구', '010-1234-5679')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (22, '서정훈', '1995-02-27', '광주시 서구', '010-2345-6780')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (23, '유지민', '1989-10-14', '울산시 중구', '010-3456-7893')",
	    		"INSERT INTO customer (cust_Id, cust_name, cust_birth, cust_addr, cust_phone) VALUES (24, '최영수', '1991-12-29', '경기도 용인시', '010-4567-8904')",

	        // item 샘플 데이터
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (1, '허니버터칩', 94, 4136, '해태', NULL, TO_DATE('2025-09-30', 'YYYY-MM-DD'), TO_DATE('2025-12-10', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (2, '꼬깔콘', 48, 2563, '롯데', NULL, TO_DATE('2025-09-29', 'YYYY-MM-DD'), TO_DATE('2026-01-07', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (3, '삼양라면', 56, 9376, '삼양', '1+1', TO_DATE('2025-04-02', 'YYYY-MM-DD'), TO_DATE('2025-05-27', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (4, '불닭볶음면', 7, 8816, '삼양', '1+1', TO_DATE('2025-11-08', 'YYYY-MM-DD'), TO_DATE('2026-08-09', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (5, '초코파이', 64, 4785, '오리온', NULL, TO_DATE('2025-05-13', 'YYYY-MM-DD'), TO_DATE('2025-06-20', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (6, '신라면', 49, 5474, '농심', NULL, TO_DATE('2025-10-23', 'YYYY-MM-DD'), TO_DATE('2025-12-21', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (7, '짜짜로니', 22, 9436, '삼양', NULL, TO_DATE('2025-04-17', 'YYYY-MM-DD'), TO_DATE('2025-12-25', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (8, '칸쵸', 87, 7427, '롯데', NULL, TO_DATE('2025-12-06', 'YYYY-MM-DD'), TO_DATE('2026-12-05', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (9, '안성탕면', 13, 8521, '농심', NULL, TO_DATE('2025-01-16', 'YYYY-MM-DD'), TO_DATE('2025-05-05', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (10, '오감자', 83, 4435, '오리온', NULL, TO_DATE('2025-01-09', 'YYYY-MM-DD'), TO_DATE('2025-11-28', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (11, '치토스', 8, 3060, '롯데', NULL, TO_DATE('2025-03-12', 'YYYY-MM-DD'), TO_DATE('2025-09-23', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (12, '맛동상', 47, 4063, '해태', NULL, TO_DATE('2025-12-18', 'YYYY-MM-DD'), TO_DATE('2026-06-08', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (13, '빼빼로', 35, 2218, '롯데', NULL, TO_DATE('2025-10-29', 'YYYY-MM-DD'), TO_DATE('2025-12-02', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (14, '감자톡', 11, 4407, '오리온', NULL, TO_DATE('2025-07-23', 'YYYY-MM-DD'), TO_DATE('2026-02-22', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (15, '예감', 32, 3935, '오리온', NULL, TO_DATE('2025-05-14', 'YYYY-MM-DD'), TO_DATE('2026-01-10', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (16, '나가사끼 짬뽕', 22, 9058, '삼양', NULL, TO_DATE('2025-12-03', 'YYYY-MM-DD'), TO_DATE('2026-02-26', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (17, '마가렛트', 96, 6153, '롯데', NULL, TO_DATE('2025-05-30', 'YYYY-MM-DD'), TO_DATE('2025-08-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (18, '초코파이', 73, 8149, '롯데', '1+1', TO_DATE('2025-11-09', 'YYYY-MM-DD'), TO_DATE('2026-02-28', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (19, '큰컵 불닭볶음면', 59, 1965, '삼양', NULL, TO_DATE('2025-08-17', 'YYYY-MM-DD'), TO_DATE('2025-11-02', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (20, '포카칩', 96, 4000, '오리온', NULL, TO_DATE('2025-09-14', 'YYYY-MM-DD'), TO_DATE('2026-08-09', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (21, '까르보 불닭볶음면', 98, 7024, '삼양', '1+1', TO_DATE('2025-07-14', 'YYYY-MM-DD'), TO_DATE('2025-11-27', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (22, '핵불닭볶음면', 52, 8494, '삼양', NULL, TO_DATE('2025-02-25', 'YYYY-MM-DD'), TO_DATE('2025-07-20', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (23, '너구리', 50, 3566, '농심', NULL, TO_DATE('2025-04-30', 'YYYY-MM-DD'), TO_DATE('2026-01-11', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (24, '짜파게티', 54, 6196, '농심', NULL, TO_DATE('2025-09-03', 'YYYY-MM-DD'), TO_DATE('2026-02-19', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (25, '감자면', 15, 6278, '농심', NULL, TO_DATE('2025-03-30', 'YYYY-MM-DD'), TO_DATE('2025-10-25', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (26, '배홍동비빔면', 8, 7041, '농심', '1+1', TO_DATE('2025-02-11', 'YYYY-MM-DD'), TO_DATE('2025-07-09', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (27, '4가지치즈 불닭볶음면', 41, 3355, '삼양', '1+1', TO_DATE('2025-02-03', 'YYYY-MM-DD'), TO_DATE('2026-01-30', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (28, '칙촉', 43, 3466, '롯데', NULL, TO_DATE('2025-07-02', 'YYYY-MM-DD'), TO_DATE('2026-04-26', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (29, '삼양우동', 10, 5254, '삼양', NULL, TO_DATE('2026-01-01', 'YYYY-MM-DD'), TO_DATE('2026-07-11', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (30, '오예스', 46, 4055, '해태', NULL, TO_DATE('2025-03-31', 'YYYY-MM-DD'), TO_DATE('2025-09-29', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (31, '빈츠', 96, 5913, '롯데', NULL, TO_DATE('2025-03-27', 'YYYY-MM-DD'), TO_DATE('2025-11-04', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (32, '에이스', 16, 4693, '해태', NULL, TO_DATE('2025-05-06', 'YYYY-MM-DD'), TO_DATE('2026-02-03', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (33, '둥지냉면', 13, 3629, '농심', NULL, TO_DATE('2025-09-09', 'YYYY-MM-DD'), TO_DATE('2026-04-04', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (34, '맛동산', 92, 6330, '해태', NULL, TO_DATE('2025-02-26', 'YYYY-MM-DD'), TO_DATE('2025-07-01', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (35, '멸치칼국수', 61, 3289, '삼양', NULL, TO_DATE('2025-05-25', 'YYYY-MM-DD'), TO_DATE('2025-07-18', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (36, '닥터유 에너지바', 99, 9158, '오리온', NULL, TO_DATE('2025-03-09', 'YYYY-MM-DD'), TO_DATE('2025-09-25', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (37, '다이제', 25, 4923, '오리온', NULL, TO_DATE('2025-10-03', 'YYYY-MM-DD'), TO_DATE('2026-01-01', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (38, '쇠고기면', 20, 5046, '삼양', NULL, TO_DATE('2025-02-21', 'YYYY-MM-DD'), TO_DATE('2025-09-22', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (39, '가나초코릿', 23, 4121, '롯데', NULL, TO_DATE('2025-03-06', 'YYYY-MM-DD'), TO_DATE('2025-06-29', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (40, '맛있는 라면', 18, 3430, '삼양', NULL, TO_DATE('2025-02-07', 'YYYY-MM-DD'), TO_DATE('2025-08-08', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (41, '삼양 짜장면', 54, 3622, '삼양', NULL, TO_DATE('2025-07-25', 'YYYY-MM-DD'), TO_DATE('2026-05-26', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (42, '삼양 돈코츠라멘', 6, 5044, '삼양', NULL, TO_DATE('2025-07-02', 'YYYY-MM-DD'), TO_DATE('2025-10-18', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (43, '홈런볼', 24, 7517, '해태', NULL, TO_DATE('2025-05-02', 'YYYY-MM-DD'), TO_DATE('2025-06-12', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (44, '촉촉한 초코칩', 65, 3186, '오리온', NULL, TO_DATE('2025-04-29', 'YYYY-MM-DD'), TO_DATE('2025-12-05', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (45, '후레쉬베리', 70, 6479, '오리온', NULL, TO_DATE('2025-07-29', 'YYYY-MM-DD'), TO_DATE('2025-09-07', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (46, '칸쵸', 90, 8127, '해태', NULL, TO_DATE('2025-12-30', 'YYYY-MM-DD'), TO_DATE('2026-02-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (47, '삼양 비빔면', 84, 7681, '삼양', NULL, TO_DATE('2025-08-15', 'YYYY-MM-DD'), TO_DATE('2026-02-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (48, '삼양 해물라면', 80, 5422, '삼양', '1+1', TO_DATE('2025-10-01', 'YYYY-MM-DD'), TO_DATE('2026-04-23', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (49, '튀김우동', 97, 2357, '농심', NULL, TO_DATE('2025-11-03', 'YYYY-MM-DD'), TO_DATE('2026-03-22', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (50, '사리곰탕면', 33, 6799, '농심', NULL, TO_DATE('2025-05-30', 'YYYY-MM-DD'), TO_DATE('2025-10-21', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (51, '자일리톨 껌', 48, 2968, '롯데', NULL, TO_DATE('2025-09-14', 'YYYY-MM-DD'), TO_DATE('2026-04-24', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (52, '오징어짬뽕', 22, 4727, '농심', NULL, TO_DATE('2025-12-27', 'YYYY-MM-DD'), TO_DATE('2026-12-08', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (53, '육개장 사발면', 89, 5700, '농심', NULL, TO_DATE('2025-11-16', 'YYYY-MM-DD'), TO_DATE('2026-04-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (54, '초코송이', 52, 6406, '오리온', NULL, TO_DATE('2025-03-03', 'YYYY-MM-DD'), TO_DATE('2025-12-30', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (55, '삼양 튀김우동', 96, 2560, '삼양', NULL, TO_DATE('2025-02-28', 'YYYY-MM-DD'), TO_DATE('2025-06-08', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (56, '김치사발면', 45, 8599, '농심', NULL, TO_DATE('2025-02-26', 'YYYY-MM-DD'), TO_DATE('2026-01-05', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (57, '삼양 사리면', 49, 8248, '삼양', '1+1', TO_DATE('2025-11-04', 'YYYY-MM-DD'), TO_DATE('2026-09-07', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (58, '왕꿈틀이', 72, 4062, '오리온', NULL, TO_DATE('2025-11-24', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (59, '삼양 참깨라면', 12, 4478, '삼양', NULL, TO_DATE('2025-05-10', 'YYYY-MM-DD'), TO_DATE('2026-03-24', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (60, '컵누들', 29, 8226, '삼양', NULL, TO_DATE('2025-08-29', 'YYYY-MM-DD'), TO_DATE('2026-06-06', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (61, '초코틴틴', 7, 8806, '해태', NULL, TO_DATE('2025-11-06', 'YYYY-MM-DD'), TO_DATE('2026-07-31', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (62, '신라면 블랙', 73, 8505, '삼양', NULL, TO_DATE('2025-09-24', 'YYYY-MM-DD'), TO_DATE('2026-09-06', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (63, '후라보노 껌', 54, 6033, '롯데', NULL, TO_DATE('2025-12-03', 'YYYY-MM-DD'), TO_DATE('2026-04-27', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (64, '후렌치파이', 24, 4129, '해태', NULL, TO_DATE('2025-11-03', 'YYYY-MM-DD'), TO_DATE('2026-01-20', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (65, '짜왕', 22, 1407, '농심', '1+1', TO_DATE('2025-02-19', 'YYYY-MM-DD'), TO_DATE('2025-06-22', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (66, '볶음너구리', 24, 2427, '농심', NULL, TO_DATE('2025-02-11', 'YYYY-MM-DD'), TO_DATE('2025-10-05', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (67, '맛짬뽕', 54, 2038, '농심', '1+1', TO_DATE('2025-08-28', 'YYYY-MM-DD'), TO_DATE('2026-07-07', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (68, '고래밥', 94, 1039, '오리온', '1+1', TO_DATE('2025-04-14', 'YYYY-MM-DD'), TO_DATE('2025-05-21', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (69, '짜파볶이', 22, 2729, '농심', '1+1', TO_DATE('2025-04-18', 'YYYY-MM-DD'), TO_DATE('2026-02-25', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (70, '삼양 나가사끼탕면', 46, 7841, '삼양', NULL, TO_DATE('2025-11-02', 'YYYY-MM-DD'), TO_DATE('2025-12-22', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (71, '크림빵', 33, 8671, '해태', NULL, TO_DATE('2025-01-19', 'YYYY-MM-DD'), TO_DATE('2025-04-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (72, '비빔면', 27, 6119, '농심', NULL, TO_DATE('2025-06-17', 'YYYY-MM-DD'), TO_DATE('2025-08-12', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (73, '순한너구리', 84, 7639, '농심', NULL, TO_DATE('2025-10-19', 'YYYY-MM-DD'), TO_DATE('2026-06-01', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (74, 'ABC 초콜릿', 2, 2765, '롯데', NULL, TO_DATE('2025-08-13', 'YYYY-MM-DD'), TO_DATE('2026-04-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (75, '마이구미', 89, 4910, '오리온', NULL, TO_DATE('2025-12-15', 'YYYY-MM-DD'), TO_DATE('2026-09-12', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (76, '수미칩', 27, 5125, '농심', NULL, TO_DATE('2025-11-12', 'YYYY-MM-DD'), TO_DATE('2026-03-23', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (77, '비틀즈', 85, 7306, '오리온', NULL, TO_DATE('2025-07-21', 'YYYY-MM-DD'), TO_DATE('2026-01-30', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (78, '삼양 쌀국수', 94, 6634, '삼양', NULL, TO_DATE('2025-05-09', 'YYYY-MM-DD'), TO_DATE('2025-08-18', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (79, '삼양 치즈불닭볶음면', 32, 3752, '삼양', NULL, TO_DATE('2025-07-07', 'YYYY-MM-DD'), TO_DATE('2026-01-04', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (80, '썬칩', 19, 8571, '오리온', NULL, TO_DATE('2025-03-02', 'YYYY-MM-DD'), TO_DATE('2025-07-13', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (81, '포스틱', 54, 5626, '농심', NULL, TO_DATE('2025-05-23', 'YYYY-MM-DD'), TO_DATE('2025-09-20', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (82, '닭다리', 41, 6174, '해태', NULL, TO_DATE('2025-11-24', 'YYYY-MM-DD'), TO_DATE('2026-02-26', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (83, '쌀로별', 17, 5220, '롯데', NULL, TO_DATE('2025-03-19', 'YYYY-MM-DD'), TO_DATE('2025-08-23', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (84, '쵸코허니볼', 57, 5904, '해태', NULL, TO_DATE('2025-08-05', 'YYYY-MM-DD'), TO_DATE('2026-01-17', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (85, '웨하스', 50, 2663, '해태', NULL, TO_DATE('2025-02-17', 'YYYY-MM-DD'), TO_DATE('2025-05-06', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (86, '목캔디', 49, 3422, '롯데', '1+1', TO_DATE('2025-08-04', 'YYYY-MM-DD'), TO_DATE('2026-07-19', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (87, '고소미', 79, 6573, '해태', NULL, TO_DATE('2025-02-21', 'YYYY-MM-DD'), TO_DATE('2025-12-24', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (88, '말랑카우', 28, 4236, '롯데', NULL, TO_DATE('2025-12-15', 'YYYY-MM-DD'), TO_DATE('2026-04-11', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (89, '아이비', 100, 6920, '해태', NULL, TO_DATE('2025-05-04', 'YYYY-MM-DD'), TO_DATE('2026-04-16', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (90, '드림카카오', 23, 5801, '롯데', NULL, TO_DATE('2025-12-15', 'YYYY-MM-DD'), TO_DATE('2026-12-06', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (91, '새우깡', 70, 5235, '농심', NULL, TO_DATE('2025-12-27', 'YYYY-MM-DD'), TO_DATE('2026-08-18', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (92, '드림카카오', 67, 2844, '롯데', NULL, TO_DATE('2025-10-23', 'YYYY-MM-DD'), TO_DATE('2026-03-02', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (93, '통크', 50, 4161, '오리온', NULL, TO_DATE('2025-09-09', 'YYYY-MM-DD'), TO_DATE('2026-07-27', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (94, '양파링', 32, 1829, '농심', '1+1', TO_DATE('2025-08-04', 'YYYY-MM-DD'), TO_DATE('2025-12-06', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (95, '삼양 로제불닭볶음면', 20, 4049, '삼양', NULL, TO_DATE('2025-04-03', 'YYYY-MM-DD'), TO_DATE('2025-09-08', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (96, '감자깡', 7, 9650, '농심', NULL, TO_DATE('2025-12-04', 'YYYY-MM-DD'), TO_DATE('2026-08-09', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (97, '레몬비스킷', 65, 9130, '해태', NULL, TO_DATE('2025-02-24', 'YYYY-MM-DD'), TO_DATE('2026-01-29', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (98, '피넛샌드', 62, 6473, '해태', NULL, TO_DATE('2025-07-09', 'YYYY-MM-DD'), TO_DATE('2026-04-05', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (99, '빠다코코낫', 62, 3546, '롯데', NULL, TO_DATE('2025-08-13', 'YYYY-MM-DD'), TO_DATE('2026-06-26', 'YYYY-MM-DD'))",
	    		"INSERT INTO item (item_id, item_name, item_qty, item_price, item_manuf, item_promo, item_made_date, item_exp_date) VALUES (100, '꿀꽈배기', 64, 6327, '농심', NULL, TO_DATE('2025-07-12', 'YYYY-MM-DD'), TO_DATE('2025-11-27', 'YYYY-MM-DD'))",
	       
	    		// orders 샘플 데이터
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (1, 3, '1234-5678-9012-3456', '카드', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (2, 7, NULL, '현금', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (3, 10, '9876-5432-1098-7654', '카드', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (4, 2, NULL, '현금', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (5, 15, '1111-2222-3333-4444', '카드', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (6, 0, NULL, '현금', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (7, 5, '5555-6666-7777-8888', '카드', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (8, 19, NULL, '현금', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (9, 24, '9999-0000-1111-2222', '카드', CURRENT_TIMESTAMP)",
	    		"INSERT INTO orders (orders_id, cust_Id, card_no, pay_type, created) VALUES (10, 8, NULL, '현금', CURRENT_TIMESTAMP)",
	      
	    		
	    		// orders_detail
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (1, 1, 3, '삼양라면', 2, '1+1', 9376 * 2)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (2, 2, 1, '허니버터칩', 1, NULL, 4136)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (3, 3, 5, '초코파이', 3, NULL, 4785 * 3)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (4, 4, 2, '꼬깔콘', 1, NULL, 2563)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (5, 5, 4, '불닭볶음면', 2, '1+1', 8816 * 2)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (6, 6, 6, '신라면', 1, NULL, 5474)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (7, 7, 8, '칸쵸', 2, NULL, 7427 * 2)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (8, 8, 9, '안성탕면', 1, NULL, 8521)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (9, 9, 10, '오감자', 4, NULL, 4435 * 4)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (10, 10, 7, '짜짜로니', 3, NULL, 9436 * 3)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (11, 1, 11, '치토스', 2, NULL, 3060 * 2)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (12, 2, 12, '맛동상', 1, NULL, 4063)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (13, 3, 13, '빼빼로', 5, NULL, 2218 * 5)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (14, 4, 14, '감자톡', 2, NULL, 4407 * 2)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (15, 5, 15, '예감', 3, NULL, 3935 * 3)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (16, 6, 16, '나가사끼 짬뽕', 1, NULL, 9058)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (17, 7, 17, '마가렛트', 4, NULL, 6153 * 4)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (18, 8, 18, '초코파이', 2, '1+1', 8149 * 2)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (19, 9, 19, '큰컵 불닭볶음면', 1, NULL, 1965)",
	    		"INSERT INTO orders_detail (orders_detail_id, orders_id, item_id, orders_item, orders_qty, orders_promo, orders_price) VALUES (20, 10, 20, '포카칩', 2, NULL, 4000 * 2)"
	        
	    };

	    try {
	    	 Connection conn = ConnectionProvider.getConnection("c##kostaco", "kostaco");
	         Statement stmt = conn.createStatement();
	         for (String sql : insertStatements) {	            
	                stmt.executeUpdate(sql);
	                System.out.println("Inserted: " + sql);
	         }
	         ConnectionProvider.close(conn, stmt);
	    } catch (Exception e) {
	    	System.out.println("예외발생:"+e.getMessage());
	    }
	}
}




