package common;

import dao.CustomerDAO;
import dao.ItemDAO;
import dao.OrdersDAO;
import dao.OrdersDetailDAO;
import vo.CustomerVO;
import vo.ItemVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunMenu {

	CustomerDAO customerDAO = new CustomerDAO();
	ItemDAO itemDAO = new ItemDAO();
	OrdersDAO ordersDAO = new OrdersDAO();
	OrdersDetailDAO ordersDetailDAO = new OrdersDetailDAO();

	//item 변수
	int itemId, itemQty, itemPrice;
	String itemName, itemManuf, itemPromo;



    //customer 변수
    String custName, custBirth, custAddr, custPhone; //상품 정보를 담을 변수
    ArrayList<CustomerVO> custList;

    String ordersDetailName;

    int re; //menu ==> 메뉴 선택을 위한 변수

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);  // 숫자라면 정수로 파싱 성공
            return true;
        } catch (NumberFormatException e) {
            return false;  // 숫자가 아니면 예외 발생
        }
    }
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
	           System.out.println("========================================");
	           System.out.println("║       🛒 KOSTACO 매장관리시스템 🛒      ║");
	           System.out.println("========================================");
	           System.out.println("========================================");
	           System.out.println(" ║ 1. 상품 등록                          ║");
	           System.out.println(" ║ 2. 상품 조회                          ║");
	           System.out.println(" ║ 3. 전체 상품 조회                      ║");
	           System.out.println(" ║ 4. 상품 삭제                          ║");
	           System.out.println(" ║ 5. 회원 등록                          ║");
	           System.out.println(" ║ 6. 회원 조회                          ║");
	           System.out.println(" ║ 7. 회원 삭제                          ║");
	           System.out.println(" ║ 8. 구매 내역 조회 (영수증)              ║");
	           System.out.println(" ║ 9. 상품 구매                          ║");
	           System.out.println(" ║ 0. 종료                              ║");
	           System.out.println("=============================KOSTACO====");

	           System.out.print("입력 : ");
	           int choice = sc.nextInt();
	           sc.nextLine();

	           switch (choice) {
                // 상품 등록
               		case 1:
               			System.out.print("상품명 : ");
               			itemName = sc.nextLine();

               			System.out.print("제조사 : ");
               			itemManuf = sc.nextLine();

	                    System.out.print("수량 : ");
	                    itemQty = sc.nextInt();

	                    System.out.print("가격 : ");
	                    itemPrice = sc.nextInt();
	                    sc.nextLine(); // consume the newline character

	                    System.out.print("프로모션: ");
	                    itemPromo = sc.nextLine();

	                    itemDAO.insertItem(itemName, itemQty, itemPrice, itemManuf, itemPromo);
	                    break;

               		case 2:
                    // 상품 ID 또는 이름을 입력하여 조회
                    System.out.println("제품 ID 또는 이름을 입력해주세요.");
                    String input = sc.nextLine();

                    if (isInteger(input)) {
	                        // 숫자면 ID로 조회
	                        int productId = Integer.parseInt(input);
	                        ItemVO itemVO = itemDAO.findById(productId);
	                        if (itemVO != null) {
	                            System.out.println("제품 ID: " + itemVO.getItemId());
	                            System.out.println("제품 이름: " + itemVO.getItemName());
	                            System.out.println("제품 수량: " + itemVO.getItemQty());
	                            System.out.println("제품 가격: " + itemVO.getItemPrice());
	                            System.out.println("제품 제조사: " + itemVO.getItemManuf());
	                            System.out.println("제품 프로모션: " + itemVO.getItemPromo());
	                            System.out.println("제품 제조일: " + itemVO.getItemMadeDate());
	                            System.out.println("제품 유효기간: " + itemVO.getItemExpDate());
	                        } else {
	                            System.out.println("해당 ID의 제품이 없습니다.");
	                        }
                    } else {
	                        // 문자열이면 이름으로 조회
                    	String name = input;
	                    ItemVO itemVO = itemDAO.findByName(name);
	                    if (itemVO==null) {
	                    	System.out.println("해당 이름의 제품이 없습니다.");
	                    }
	                    else {
	                    	System.out.println("제품 ID: " + itemVO.getItemId());
                            System.out.println("제품 이름: " + itemVO.getItemName());
                            System.out.println("제품 수량: " + itemVO.getItemQty());
                            System.out.println("제품 가격: " + itemVO.getItemPrice());
                            System.out.println("제품 제조사: " + itemVO.getItemManuf());
                            System.out.println("제품 프로모션: " + itemVO.getItemPromo());
                            System.out.println("제품 제조일: " + itemVO.getItemMadeDate());
                            System.out.println("제품 유효기간: " + itemVO.getItemExpDate());
                            System.out.println("-------------------------------");
                            }
                    }
                    break;
                case 3:
                	// 전체 상품 조회
                    List<ItemVO> items = itemDAO.findAllItem();
                    for (ItemVO item : items) {
                        System.out.println("제품 ID: " + item.getItemId());
                        System.out.println("제품 이름: " + item.getItemName());
                        System.out.println("제품 수량: " + item.getItemQty());
                        System.out.println("제품 가격: " + item.getItemPrice());
                        System.out.println("제품 제조사: " + item.getItemManuf());
                        System.out.println("제품 프로모션: " + item.getItemPromo());
                        System.out.println("제품 제조일: " + item.getItemMadeDate());
                        System.out.println("제품 유효기간: " + item.getItemExpDate());
                        System.out.println("-------------------------------");
                    }
                    break;
                case 4:
                    System.out.println("제품을 입력해주세요.");
                    itemDAO.deleteByName(sc.nextLine());
                    break;
                case 5 :
                    //회원 등록
                    System.out.print("회원 이름 : ");
                    custName = sc.nextLine();
                    System.out.print("생년월일 : [입력예시 : 2000/00/00]");
                    custBirth = sc.nextLine();
                    System.out.print("주소 : ");
                    custAddr = sc.nextLine();
                    System.out.print("전화번호 : ");
                    custPhone = sc.nextLine();
                    re = customerDAO.insertCustomer(custName, custBirth, custAddr, custPhone);
                    if(re != -1)
                        System.out.println("회원이 등록되었습니다.");
                    else System.out.println("등록을 실패하였습니다.");
                    break;

                case 6 :
                    //회원 조회
                    int index;
                    System.out.println("1. 이름과 핸드폰 뒷자리(4개)로 조회");
                    System.out.println("2. 전체회원 조회");
                    System.out.println("3. 특정날자,N번째 고객 조회");
                    System.out.println("=============================");
                    System.out.print("입력 =>");
                    index = sc.nextInt();
                    switch(index) {
                        case 1:
                            //회원 삭제 회원이름과 핸드폰 뒷자리4개를 받아서 조회한다.
                            System.out.println("조회할 회원 이름 : ");
                            custName = sc.next();
                            System.out.println("핸드폰 뒷자리 4개 : ");
                            custPhone = sc.next();
                            int custId = customerDAO.findCustByNameAndPhone(custName, custPhone);
                            CustomerVO custVo =  customerDAO.getCustByCustid(custId);
                            if(custVo != null) {
                                System.out.println("고객번호 : " + custVo.getCustId() +
                                        " 고객이름 : " + custVo.getCustName() +
                                        "\n고객주민 : " + custVo.getCustBirth() +
                                        "\n고객주소 : " + custVo.getCustAddr() +
                                        "\n고객연락처 : " + custVo.getCustPhone());
                                System.out.println();
                            }

                            break;
                        case 2:
                            //전체회원 조회

                            System.out.println("전체 회원 리스트");
                            custList = customerDAO.findAllCust();
                            for(CustomerVO vo : custList) {
                                System.out.println("고객번호 : " + vo.getCustId() +
                                        " 고객이름 : " + vo.getCustName() +
                                        "\n고객주민 : " + vo.getCustBirth() +
                                        "\n고객주소 : " + vo.getCustAddr() +
                                        "\n고객연락처 : " + vo.getCustPhone());
                                System.out.println();
                            }
                            break;
                      case 3:
                            String date;
                            int N;
                            int id;
                            System.out.print("조회할 날자를 입력 =>");
                            date = sc.next();
                            System.out.print("몇번째 주문한 고객을 조회 하시겠습니까 =>");
                            N = sc.nextInt();
                            id = customerDAO.findCustIdByDateAndN(date, N);
                            CustomerVO custVo2 =  customerDAO.getCustByCustid(id);
                            if(custVo2 != null) {
                                System.out.println("고객번호 : " + custVo2.getCustId() +
                                        " 고객이름 : " + custVo2.getCustName() +
                                        "\n고객주민 : " + custVo2.getCustBirth() +
                                        "\n고객주소 : " + custVo2.getCustAddr() +
                                        "\n고객연락처 : " + custVo2.getCustPhone());
                                System.out.println();
                                break;
                            }
                    }
                    break;

                case 7 :

                    //회원삭제
                    String custName;
                    String custPhone;
                    System.out.print("삭제할 회원의 이름 => ");
                    custName = sc.next();
                    System.out.println("회원 핸드폰 뒷자리 4개 =>");
                    custPhone = sc.next();
                    int custId2 = customerDAO.findCustByNameAndPhone(custName, custPhone);
                    int rs = customerDAO.deleteCustomer(custId2);
                    if(rs != -1) {
                        System.out.println("회원 삭제 성공!");
                    }else {
                        System.out.println("회원 삭제 실패!");
                    }
                    break;
          		case 8 :
        			System.out.print("조회할 회원의 이름을 입력하세요. [회원이 아닐 시 비회원 입력] ==> ");
        			ordersDetailName = sc.next();
        			//ordersDetailDAO.printReceipt(ordersDetailName);
        			break;
        		case 9 :
        			int ordersId, itemId, orderQty, custId;
        			String payType, cardNo;

                    System.out.print("고객명을 입력하세요.[비회원이면 비회원 입력]==> ");
                    custName = sc.next();
                    sc.nextLine();

                    if(custName.equals("비회원")) custId = 0;
                    else {
                    	System.out.print("핸드폰 뒷자리 입력");
                    	custPhone = sc.next();
                    	custId = customerDAO.findCustByNameAndPhone(custName, custPhone);
                    }

        			System.out.print("결제 수단을 입력하세요.[현금/카드]==> ");
        			payType = sc.next();
        			sc.nextLine();

        			System.out.print("카드 번호를 입력하세요. [현금: null 또는 0000]==> ");
        			cardNo = sc.nextLine();


        			ordersId = ordersDAO.insertOrders(custId, cardNo, payType);

                    //ordersId = ordersDAO.getOrdersId();
        			if(ordersId > 0) {
	        			while(true) {
	        				List<ItemVO> itemlist = itemDAO.findAllItem();

	        				System.out.println("-------------------------------------");
	        				System.out.println("║            전체 상품 목록            ║");
	        				System.out.println("-------------------------------------");
	        				System.out.printf("║번호 이름   수량 프로모션  가격          ║\n");

	        				for (ItemVO item : itemlist) {
	        				    System.out.printf(" %d. %-2s %d개 %2s %,3d원\n",
	        				            item.getItemId(),
	        				            item.getItemName(),
	        				            item.getItemQty(),
	        				            item.getItemPromo(),
	        				            item.getItemPrice());
	        				}
	        				System.out.println("║                                    ║");
	        				System.out.println("-------------------------------------");
		        			System.out.print("구매할 상품을 선택해주세요.[0 입력 시 구매 종료]==> ");
		        			itemId = sc.nextInt();
		        			if(itemId == 0) break;
		        			sc.nextLine();


		        			System.out.print("구매할 수량을 입력해주세요.==> ");
		        			orderQty = sc.nextInt();
		        			sc.nextLine();

		        			ordersDetailDAO.insertReceipt(ordersId, itemId, orderQty);
		        			ItemVO item = itemDAO.findById(itemId); // 상품 정보 가져오기

		        			int displayQty = orderQty;
		        			if ("1+1".equals(item.getItemPromo())) {
		        			    displayQty = orderQty * 2; // 프로모션이면 수량 2배 차감
		        			}
		        			itemDAO.updateItem(itemId, displayQty); // 재고 차감
	        			}
        			}else {
        				System.out.println("입력 오류");
        				break;
        			}

        			//ordersDetailDAO.printReceipt(ordersId);
        			break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 입력해 주세요.");
            }
        }
    }
}