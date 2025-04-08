package common;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dao.CustomerDAO;
import dao.ItemDAO;
import dao.OrdersDAO;
import dao.OrdersDetailDAO;
import vo.ItemVO;
import vo.OrdersDetailVO;
import vo.OrdersVO;

public class OrdersRun {
	public void orderRun(Scanner sc) {
		CustomerDAO customerDAO = new CustomerDAO();
		ItemDAO itemDAO = new ItemDAO();
		OrdersDAO ordersDAO = new OrdersDAO();
		OrdersDetailDAO ordersDetailDAO = new OrdersDetailDAO();
		int total, count;
		
		while (true) {
		    System.out.println("╔═════════════════════════════════╗");
		    System.out.println("        🛒 KOSTACO 주문관리     ");
		    System.out.println("╠═════════════════════════════════╣");
			System.out.println("1. 주문 등록");
			System.out.println("2. 주문 삭제");
			System.out.println("3. 주문 조회");
			System.out.println("4. 구매하지 않은 인기 상품");
			System.out.println("5. 구매하지 않은 추천 상품");
			System.out.println("0. 종료");
            System.out.println("╠══════════════════════KOSTACO════╣");

			System.out.print("선택: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {

			case 1:
				boolean isOrdersItem = false;
				int custId, itemId;
				String custName, custPhone;
				String payType, cardNo;
				String itemName; 
				String promo;
				int ordersId, ordersQty, itemQty, selectedItemQty, ordersPrice;
				total = 0;
				count = 0;
				System.out.print("고객명을 입력하세요.[비회원이면 비회원 입력]==> ");
				custName = sc.next();
				sc.nextLine();

				if (custName.equals("비회원"))
					custId = 0;
				else {
					System.out.print("핸드폰 뒷자리 입력: ");
					custPhone = sc.next();
					custId = customerDAO.findCustByNameAndPhone(custName, custPhone);
				}
				if (custId < 0) {
					System.out.println("고객 정보가 존재하지 않습니다.");
					return;
				}//0보다 작으면 회원이 없다
				
				System.out.print("결제 수단을 입력하세요.[현금/카드]==> ");
				payType = sc.next();
				sc.nextLine();

				System.out.print("카드 번호를 입력하세요. [현금: 엔터 또는 0000]==> ");
				cardNo = sc.nextLine();

				if (cardNo.trim().isEmpty() || cardNo.equalsIgnoreCase("null")) {
					cardNo = "0000";
				}//현금 일 때 엔터거나 null이면 0000으로 카드번호 저장

				ordersId = ordersDAO.insertOrders(custId, cardNo, payType);
				
				if (ordersId <= 0) {
					System.out.println("주문 등록 실패");
					return;
				}//0보다 작으면 주문 등록 실패
				
				while (true) {
					List<ItemVO> itemlist = itemDAO.findAllItem();
	    		    System.out.println("╔════════════════════════════════════════╗");
	    		    System.out.println("            🛒 KOSTACO 상품 목록            ");
	    		    System.out.println("╠════════════════════════════════════════╣");
	    		    System.out.printf("%s   %-7s %-4s  %-7s %-6s \n", "번호", "이름", "수량", "프로모션", "금액");

					for (ItemVO item : itemlist) {
						System.out.printf("%d.   %-7s %-4d     %-7s %,d원 \n", item.getItemId(), item.getItemName(),
								item.getItemQty(), ("1+1".equals(item.getItemPromo()) ? "1+1" : " - "), item.getItemPrice());
					}//상품 목록 출력 for문 종료
					System.out.println("╠════════════════════════════KOSTACO═════╣");
					
					System.out.print("구매할 상품을 선택해주세요.[0 입력 시 구매 종료]==> ");
					itemId = sc.nextInt();
					
					if (itemId == 0) break;
					
					if (itemId >= itemDAO.getNextNo()) {
					        System.out.println("❗ 존재하지 않는 상품입니다. 다시 선택해주세요.");
					        continue;
					}//존재 상품 여부 확인 종료
					ItemVO item = itemDAO.findById(itemId);
					itemName = item.getItemName();
					System.out.print("구매할 수량을 입력해주세요.==> ");
					ordersQty = sc.nextInt();
					sc.nextLine();
						
					itemQty = item.getItemQty();
					selectedItemQty = ordersQty;
					promo = " - ";
					if("1+1".equals(item.getItemPromo())){
						promo = "1+1";
						selectedItemQty *= 2;
					}
					if(selectedItemQty > itemQty){
						System.out.println("재고가 부족합니다. 현재 구매 가능 개수 : " + itemQty);
						continue;
					}//재고 수량 확인 if문 종료
					ordersPrice = ordersQty * item.getItemPrice();
					//if(custId > 0 ) ordersPrice = (int) (ordersPrice * (0.9));
					
					ordersDetailDAO.insertReceipt(ordersId, itemId, itemName, selectedItemQty, promo, ordersPrice);
					itemDAO.updateItem(itemId, ordersDetailDAO.findByOrdersIdAndItemId(ordersId, itemId));
					isOrdersItem = true; // 주문이 하나라도 담겼으면 true
				}//while 종료
				
				//상품을 담지 않으면 위에서 orders 테이블에 추가한 데이터 삭제
				if(!isOrdersItem) {
					ordersDAO.deleteOrders(ordersId);
					System.out.println("주문 내역이 없어 주문이 취소되었습니다.");
					break; //삭제 후 종료
				}
				
				//영수증 출력
    		    System.out.println("╔════════════════════════════════════════╗");
    		    System.out.println("            🛒 KOSTACO 영수증              ");
    		    System.out.println("╠════════════════════════════════════════╣");
    		    System.out.printf(" %-8s : %-25s\n", "고객명", custName);
    		    System.out.printf(" %-8s : %-25s\n", "결제 날짜", ordersDAO.getCreated(ordersId));
    		    System.out.printf(" %-8s : %-25d\n", "주문 번호", ordersId);
    		    System.out.printf(" %-8s : %-25s\n", "결제 수단", payType);
    		    System.out.printf(" %-8s : %-25s\n", "카드 번호", cardNo);
    		    System.out.println("╠════════════════════════════════════════╣");
    		    System.out.printf("  %-10s %-6s   %-8s %-6s \n", "상품명", "수량", "프로모션", "금액");
    		    System.out.println("╠════════════════════════════════════════╣");
				List<OrdersDetailVO> odlist = ordersDetailDAO.receiptList(ordersId);
				total = 0;
				count = 0;
				//리스트에 저장한 상품 한줄씩 출력
				for(OrdersDetailVO odvo : odlist) {
					System.out.printf("  %-10s %-6d     %-8s %,d원 \n", odvo.getOrdersItem(), odvo.getOrdersQty(), odvo.getOrdersPromo(), odvo.getOrdersPrice());
					total += odvo.getOrdersPrice(); //상품에 맴버십 처리된 가격 합
					count += odvo.getOrdersQty(); //상품에 프로모션 처리된 수량의 합
				}
				if(custId > 0) {
					total *= 0.9;
				}
		        System.out.println("╠════════════════════════════════════════╣");
		        System.out.printf("  %-10s : %6d개 / %,6d원 \n", "합계/금액", count, total);
				System.out.println("╠════════════════════════════KOSTACO═════╣");

				break;
			
			case 2:
				//삭제시 이름, 전화번호, 날짜를 찾아서 담을 id 변수
				String deleteCustName, deleteCustPhone, deleteCreated;
				int deleteCustId;
				
				System.out.println("검색할 고객 이름 입력: ");
				deleteCustName = sc.next();

				System.out.println("회원 핸드폰 뒷자리 4개 =>");
				deleteCustPhone = sc.next();
				
				deleteCustId = customerDAO.findCustByNameAndPhone(deleteCustName, deleteCustPhone);
				
				//삭제할 회원의 이름과 전화번호로 id를 받아 날짜와 함께 ordersid를 조회하여 해당하는 주문 내역의 조회한다

				if(deleteCustId >= 0) {
					System.out.print("조회 날짜 => [입력 예시: YYYY/MM/DD]");
					deleteCreated = sc.next();
					
					List<OrdersVO> deletecustomerOrders = ordersDAO.findOrdersByCustIdAndCreated(deleteCustId, deleteCreated);
					if (deletecustomerOrders.isEmpty()) {
						System.out.println("해당 고객의 주문이 없습니다.");
					} else {

						for (OrdersVO order : deletecustomerOrders) {
				
			    		    System.out.println("╔════════════════════════════════════════╗");
			    		    System.out.println("            🛒 KOSTACO 영수증              ");
			    		    System.out.println("╠════════════════════════════════════════╣");
			    		    System.out.printf(" %-8s : %-25s\n", "고객명", deleteCustName);
			    		    System.out.printf(" %-8s : %-25s\n", "결제 날짜", ordersDAO.getCreated(order.getOrdersId()));
			    		    System.out.printf(" %-8s : %-25d\n", "주문 번호", order.getOrdersId());
			    		    System.out.printf(" %-8s : %-25s\n", "결제 수단", order.getPayType());
			    		    System.out.printf(" %-8s : %-25s\n", "카드 번호", order.getCardNo());
			    		    System.out.println("╠════════════════════════════════════════╣");
			    		    System.out.printf("  %-10s %-6s   %-8s %-6s \n", "상품명", "수량", "프로모션", "금액");
			    		    System.out.println("╠════════════════════════════════════════╣");
							total = 0;
							count = 0;
							List<OrdersDetailVO> deleteList = ordersDetailDAO.receiptList(order.getOrdersId());
							for(OrdersDetailVO odvo : deleteList) {
								System.out.printf("  %-10s %-6d     %-8s %,d원 \n", odvo.getOrdersItem(), odvo.getOrdersQty(), odvo.getOrdersPromo(), odvo.getOrdersPrice());
								total += odvo.getOrdersPrice();
								count += odvo.getOrdersQty();
							}
					        System.out.println("╠════════════════════════════════════════╣");
					        System.out.printf("  %-10s : %6d개 / %,6d원 \n", "합계/금액", count, total);				  
							System.out.println("╠════════════════════════════KOSTACO═════╣");
						}
					}
					//조회된 주문 내역 중에 주문 번호를 보고 주문번호를 입력하여 삭제
					System.out.println("삭제할 주문 ID 입력: ");
					int deleteId = sc.nextInt();
					int result = ordersDAO.deleteOrders(deleteId);
					if (result > 0) {
						System.out.println("주문이 삭제되었습니다.");
					} else {
						System.out.println("주문 삭제 실패.");
					}
				}else {
					System.out.println("해당 고객은 존재하지 않습니다.");
				}
				
				
				break;

			case 3:
				String searchCustName, searchCustPhone, searchCreated;
				int searchCustId;
				
				System.out.print("검색할 고객 이름 입력: ");
				searchCustName = sc.next();

				System.out.print("회원 핸드폰 뒷자리 4개: ");
				searchCustPhone = sc.next();
				
				searchCustId = customerDAO.findCustByNameAndPhone(searchCustName, searchCustPhone);
				if(searchCustId >= 0) {
					System.out.print("조회 날짜 => [입력 예시: YYYY/MM/DD]");
					searchCreated = sc.next();
					
					List<OrdersVO> findCustomerOrders = ordersDAO.findOrdersByCustIdAndCreated(searchCustId, searchCreated);
					if (findCustomerOrders.isEmpty()) {
						System.out.println("해당 고객의 주문이 없습니다.");
					} else {

						for (OrdersVO order : findCustomerOrders) {
				
			    		    System.out.println("╔════════════════════════════════════════╗");
			    		    System.out.println("            🛒 KOSTACO 영수증              ");
			    		    System.out.println("╠════════════════════════════════════════╣");
			    		    System.out.printf("%-8s : %-25s\n", "고객명", searchCustName);
			    		    System.out.printf("%-8s : %-25s\n", "결제 날짜", ordersDAO.getCreated(order.getOrdersId()));
			    		    System.out.printf("%-8s : %-25d\n", "주문 번호", order.getOrdersId());
			    		    System.out.printf("%-8s : %-25s\n", "결제 수단", order.getPayType());
			    		    System.out.printf("%-8s : %-25s\n", "카드 번호", order.getCardNo());
			    		    System.out.println("╠════════════════════════════════════════╣");
			    		    System.out.printf("  %-10s %-6s   %-8s %-6s \n", "상품명", "수량", "프로모션", "금액");
			    		    System.out.println("╠════════════════════════════════════════╣");
							total = 0;
							count = 0;
							List<OrdersDetailVO> searchList = ordersDetailDAO.receiptList(order.getOrdersId());
							for(OrdersDetailVO odvo : searchList) {
								System.out.printf("  %-10s %-6d     %-8s %,d원 \n", odvo.getOrdersItem(), odvo.getOrdersQty(), odvo.getOrdersPromo(), odvo.getOrdersPrice());
								total += odvo.getOrdersPrice();
								count += odvo.getOrdersQty();
							}
					        System.out.println("╠════════════════════════════════════════╣");
					        System.out.printf("  %-10s : %6d개 / %,6d원 \n", "합계/금액", count, total);				  
							System.out.println("╠════════════════════════════KOSTACO═════╣");
						}
					}
					
					}else {
						System.out.println("해당 고객은 존재하지 않습니다.");
				}
			case 4 :
				String popularCustName, popularCustPhone; 
				int popularCustId;
				
				System.out.print("검색할 고객 이름 입력: ");
				popularCustName = sc.next();
				
				if (popularCustName.equals("비회원"))
					popularCustId = 0;
				else {
					System.out.print("핸드폰 뒷자리 입력: ");
					popularCustPhone = sc.next();
					popularCustId = customerDAO.findCustByNameAndPhone(popularCustName, popularCustPhone);
				}
				
				List<Integer> popularItemId = new ArrayList<Integer>();
				List<ItemVO> popularItemList = new ArrayList<ItemVO>();
				
				if (popularCustId == -1) {
				    System.out.println("해당 고객 정보를 찾을 수 없습니다.");
				    break;
				}
				popularItemId = ordersDetailDAO.getPopularItem(popularCustId);
				
				for(int no : popularItemId) {
					popularItemList.add(itemDAO.findById(no));
				}
				System.out.println("╔════════════════════════════════════════╗");
	    		System.out.println("           🛒 KOSTACO 인기 상품 목록         ");
	    		System.out.println("╠════════════════════════════════════════╣");
	    		System.out.printf("%s   %-7s %-4s  %-7s %-6s \n", "번호", "이름", "수량", "프로모션", "금액");
				for(ItemVO item : popularItemList) {
					System.out.printf("%d.   %-7s %-4d     %-7s %,d원 \n", item.getItemId(), item.getItemName(),
							item.getItemQty(), ("1+1".equals(item.getItemPromo()) ? "1+1" : " - "), item.getItemPrice());
				}
				System.out.println("╠════════════════════════════KOSTACO═════╣");
				break;
			case 5 :
				String recommendCustName, recommendCustPhone; 
				int recommendCustId;
				
				System.out.print("검색할 고객 이름 입력: ");
				recommendCustName = sc.next();
				
				if (recommendCustName.equals("비회원"))
					recommendCustId = 0;
				else {
					System.out.print("핸드폰 뒷자리 입력: ");
					recommendCustPhone = sc.next();
					recommendCustId = customerDAO.findCustByNameAndPhone(recommendCustName, recommendCustPhone);
				}
				List<Integer> recommendItemId = new ArrayList<Integer>();
				List<ItemVO> recommendItemList = new ArrayList<ItemVO>();
				
				if (recommendCustId == -1) {
				    System.out.println("해당 고객 정보를 찾을 수 없습니다.");
				    break;
				}

				
				recommendItemId = ordersDetailDAO.getRecommendItem(recommendCustId);
				
				for(int no : recommendItemId) {
					recommendItemList.add(itemDAO.findById(no));
				}
				System.out.println("╔════════════════════════════════════════╗");
	    		System.out.println("           🛒 KOSTACO 추천 상품 목록         ");
	    		System.out.println("╠════════════════════════════════════════╣");
	    		System.out.printf("%s   %-7s %-4s  %-7s %-6s \n", "번호", "이름", "수량", "프로모션", "금액");
				for(ItemVO item : recommendItemList) {
					System.out.printf("%d.   %-7s %-4d     %-7s %,d원 \n", item.getItemId(), item.getItemName(),
							item.getItemQty(), ("1+1".equals(item.getItemPromo()) ? "1+1" : " - "), item.getItemPrice());
				}
				System.out.println("╠════════════════════════════KOSTACO═════╣");
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("잘못된 입력입니다. 다시 선택하세요.");

			}
		}
	}
}
