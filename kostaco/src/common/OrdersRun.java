package common;

import java.util.List;
import java.util.Scanner;

import dao.CustomerDAO;
import dao.ItemDAO;
import dao.OrdersDAO;
import dao.OrdersDetailDAO;
import vo.ItemVO;
import vo.OrdersVO;

public class OrdersRun {
	public void orderRun(Scanner sc) {
		CustomerDAO customerDAO = new CustomerDAO();
		ItemDAO itemDAO = new ItemDAO();
		OrdersDAO ordersDAO = new OrdersDAO();
		OrdersDetailDAO ordersDetailDAO = new OrdersDetailDAO();

		int custId;
		String custName, custPhone;

		int itemId;

		String payType, cardNo;
		String ordersDetailName, ordersCreated;
		int ordersId, orderQty;

		while (true) {
			System.out.println("=====주문 관리 시스템=====");
			System.out.println("1. 주문 등록");
			System.out.println("2. 주문 삭제");
			System.out.println("3. 주문 조회");
			System.out.println("5. 종료");
			System.out.print("선택: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {

			case 1:
				System.out.print("고객명을 입력하세요.[비회원이면 비회원 입력]==> ");
				custName = sc.next();
				sc.nextLine();

				if (custName.equals("비회원"))
					custId = 0;
				else {
					System.out.print("핸드폰 뒷자리 입력");
					custPhone = sc.next();
					custId = customerDAO.findCustByNameAndPhone(custName, custPhone);
				}
				if (custId < 0) {
					System.out.println("고객 정보가 존재하지 않습니다.");
					return;
				}
				System.out.print("결제 수단을 입력하세요.[현금/카드]==> ");
				payType = sc.next();
				sc.nextLine();

				System.out.print("카드 번호를 입력하세요. [현금: 엔터 또는 0000]==> ");
				cardNo = sc.nextLine();

				if (cardNo.trim().isEmpty() || cardNo.equalsIgnoreCase("null")) {
					cardNo = "0000";
				}

				ordersId = ordersDAO.insertOrders(custId, cardNo, payType);

				if (ordersId <= 0) {
					System.out.println("주문 등록 실패");
					return;
				}
				List<ItemVO> itemlist = itemDAO.findAllItem();
				while (true) {

					System.out.println("-------------------------------------");
					System.out.println("║            전체 상품 목록            ║");
					System.out.println("-------------------------------------");
					System.out.printf("║번호 이름   수량 프로모션  가격          ║\n");

					for (ItemVO item : itemlist) {
						System.out.printf(" %d. %-2s %d개 %2s %,3d원\n", item.getItemId(), item.getItemName(),
								item.getItemQty(), item.getItemPromo(), item.getItemPrice());
					}
					System.out.print("구매할 상품을 선택해주세요.[0 입력 시 구매 종료]==> ");
					itemId = sc.nextInt();
					if (itemId == 0)
						break;

					System.out.print("구매할 수량을 입력해주세요.==> ");
					orderQty = sc.nextInt();
					sc.nextLine();

					ordersDetailDAO.insertReceipt(ordersId, itemId, orderQty);
					itemDAO.updateItem(itemId, ordersDetailDAO.findByOrdersIdAndItemId(ordersId, itemId));
				}
				ordersDetailDAO.printReceipt(ordersId, custName);
				break;

			case 2:
				System.out.println("삭제할 주문 ID 입력: ");
				int deleteId = sc.nextInt();
				int result = ordersDAO.deleteOrders(deleteId);
				if (result > 0) {
					System.out.println("주문이 삭제되었습니다.");
				} else {
					System.out.println("주문 삭제 실패.");
				}
				break;

			case 3:
				System.out.print("조회할 회원의 이름을 입력하세요. [회원이 아닐 시 비회원 입력] ==> ");
    			ordersDetailName = sc.next();
    			
    			System.out.print("조회할 날짜를 입력하세요. [입력 예시: 0000/00/00]");
    			ordersCreated = sc.next();

    			ordersDetailDAO.printReceipt(ordersDetailName, ordersCreated);
    			
    		    
			case 4:
				System.out.println("검색할 고객 ID 입력: ");
				int searchCustId = sc.nextInt();
				List<OrdersVO> customerOrders = ordersDAO.findOrdersByCustId(searchCustId);
				if (customerOrders.isEmpty()) {
					System.out.println("해당 고객의 주문이 없습니다.");
				} else {
					for (OrdersVO order : customerOrders) {
						System.out.println(order);
					}
				}
				break;
			case 5:
				System.out.println("프로그램을 종료합니다.");
				sc.close();
				return;
			default:
				System.out.println("잘못된 입력입니다. 다시 선택하세요.");

			}
		}
	}
}
