package common;

import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        ItemRun itemRun = new ItemRun();
        OrdersRun ordersRun = new OrdersRun();
        CustomerRun customerRun = new CustomerRun();

        Scanner sc = new Scanner(System.in);
        while (true) {

            System.out.println("==================================");
            System.out.println("║     🛒 KOSTACO 매장관리시스템 🛒    ");
            System.out.println("==================================");
            System.out.println("==================================");
            System.out.println("║ 1. 상품                         ");
            System.out.println("║ 2. 회원                         ");
            System.out.println("║ 3. 주문                         ");
            System.out.println("║ 0. 프로그램 종료                  ");
            System.out.println("=======================KOSTACO====");

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
            }
        }
    }
}
