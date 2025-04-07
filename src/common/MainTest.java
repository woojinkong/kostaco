package common;

import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        ItemRun itemRun = new ItemRun();
        OrdersRun ordersRun = new OrdersRun();
        CustomerRun customerRun = new CustomerRun();
        SalesStatsRun salesStatsRun = new SalesStatsRun(); 

        Scanner sc = new Scanner(System.in);
        
        
        while (true) {

		    System.out.println("╔═════════════════════════════════╗");
		    System.out.println("        🛒 KOSTACO 매장관리시스템      ");
		    System.out.println("╠═════════════════════════════════╣");
            System.out.println(" 1. 상품                         ");
            System.out.println(" 2. 회원                         ");
            System.out.println(" 3. 주문                         ");
            System.out.println(" 4. 판매 통계						");
            System.out.println(" 0. 프로그램 종료                  ");
            System.out.println("╠══════════════════════KOSTACO════╣");

            System.out.println("선택: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    itemRun.itemRun(sc);
                    break;
                case 2:
                	customerRun.runCustomer(sc);
                	break;
                case 3 :
                	ordersRun.orderRun(sc);
                	break;
                case 4 : 
                	salesStatsRun.salesStatsrun(sc);
                	break;
                case 0: 
                	System.out.println("프로그램을 종료합니다. 좋은 하루 되세요!");
                	sc.close();
                	System.exit(0);
                	break;
                default:
                	System.out.println("⚠ 잘못된 입력입니다. 다시 선택해 주세요.");
                	break;
                	
            }
        }
    }
}
