package common;


import java.util.ArrayList;
import java.util.Scanner;
import dao.CustomerDAO;
import vo.CustomerVO;

public class CustomerRun {

	public void runCustomer(Scanner sc) {
		int menu, re; //menu ==> 메뉴 선택을 위한 변수
		String cname, birth, addr, phone; //상품 정보를 담을 변수
		CustomerDAO customerDAO = new CustomerDAO();
		//===========================추가
		ArrayList<CustomerVO> custlist;
		while(true) {
			System.out.println("  =====================KOSTACO====");
			System.out.println(" ║ 1. 회원 등록                     ");
			System.out.println(" ║ 2. 회원 조회                     ");
			System.out.println(" ║ 3. 회원 수정                     ");
			System.out.println(" ║ 4. 회원 삭제                     ");
			System.out.println(" ║ 0. 종료                         ");
			System.out.println("  =====================KOSTACO====");
			System.out.print("입력 : ");
			menu = sc.nextInt();
			sc.nextLine();
			System.out.println();
			switch(menu) {
			case 1 : 
				//회원 등록
				System.out.println("  =====================KOSTACO====");
				System.out.println(" ║             고객등록              ");
				System.out.println("  =====================KOSTACO====");
				System.out.print("회원 이름 : ");
				cname = sc.nextLine();
				System.out.print("생년월일 : [입력예시 : 2000/00/00]");
				birth = sc.nextLine();
				System.out.print("주소 : ");
				addr = sc.nextLine();
				System.out.print("전화번호 : ");
				phone = sc.nextLine();
				re = customerDAO.insertCustomer(cname, birth, addr, phone);
				if(re != -1)
					System.out.println("회원이 등록되었습니다.");
				else System.out.println("등록을 실패하였습니다.");
				break;

			case 2 : 
				//회원 조회
				int index;
				System.out.println("  =====================KOSTACO====");
				System.out.println(" ║             회원 조회            ");
				System.out.println("  =====================KOSTACO====");
				System.out.println("1. 이름과 핸드폰 뒷자리(4개)로 조회");
				System.out.println("2. 전체회원 조회");
				System.out.println("3. 특정날자, N번째 고객 조회");
				System.out.println("4. 특정기간, 매출 VIP 고객 조회");
				System.out.println("5. 특정상품, VIP 고객 조회");
				System.out.println("=============================");
				System.out.print("입력 =>");
				index = sc.nextInt();
				switch(index) {
				case 1:
					//회원 삭제 회원이름과 핸드폰 뒷자리4개를 받아서 조회한다.
					System.out.println("조회할 회원 이름 : ");
					cname = sc.next();
					System.out.println("핸드폰 뒷자리 4개 : ");
					phone = sc.next();
					int custid = customerDAO.findCustByNameAndPhone(cname, phone);
					CustomerVO custVo =  customerDAO.getCustByCustid(custid);
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
					System.out.println("  =====================KOSTACO====");
					System.out.println(" ║             전체고객조회           ");
					System.out.println("  =====================KOSTACO====");
					custlist = customerDAO.findAllCust();
					for(CustomerVO vo : custlist) {
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
					int custId;
					System.out.println("  =====================KOSTACO====");
					System.out.println(" ║             회원 조회            ");
					System.out.println("  =====================KOSTACO====");
					System.out.print("조회할 날자를 입력 =>");
					date = sc.next();
					System.out.print("몇번째 주문한 고객을 조회 하시겠습니까 =>");
					N = sc.nextInt();
					custId = customerDAO.findCustIdByDateAndN(date, N);
					CustomerVO custVo2 =  customerDAO.getCustByCustid(custId);
					if(custVo2 != null) {
						System.out.println("고객번호 : " + custVo2.getCustId() + 
								" 고객이름 : " + custVo2.getCustName() + 
								"\n고객주민 : " + custVo2.getCustBirth() + 
								"\n고객주소 : " + custVo2.getCustAddr() + 
								"\n고객연락처 : " + custVo2.getCustPhone());
								System.out.println();
					}
					break;
					
				case 4:
					//특정기간 VIP 고객
					//특정기간 동안 주문금액이 가장 많은 고객 순으로 조회한다.
					String date1;
					String date2;
					int count;
					int index3 = 1;
					
					System.out.println("  =====================KOSTACO====");
					System.out.println(" ║             매출VIP고객조회        ");
					System.out.println("  =====================KOSTACO====");
					System.out.println("특정기간동안 구매금액이 많은 VIP고객을 조회합니다.");
					System.out.println("===============================");
					System.out.print("시작날자를 입력해주세요[예:2024/12/05] =>");
					date1 = sc.next();
					System.out.print("마감날자를 입력해주세요[예:2025/12/25] =>");
					date2 = sc.next();
					System.out.print("몇번째 고객까지 보겠습니까 =>[예:10]");
					count = sc.nextInt();
					ArrayList<CustomerVO> list = customerDAO.findVipCustomer(date1, date2, count);
					
					System.out.println("======VIP고객 리스트======");
					
					
					for(CustomerVO vo : list) {
						System.out.println(index3 + " 이름 : " + vo.getCustName() + "| 총구매금액 : " + vo.getCustSalePrice());
						index3++;
					}

					break;
				case 5:
					//특성상품을 가장 많이 구입한 고객리스트를 가져오는 기능.
					//itemid와 몇번째 고객까지 출력할지 를 매개변수로 넣어주면 리스트로 반환된다.
					String itemName;
					int custCount;
					int index2 = 1;
					System.out.println("  =====================KOSTACO====");
					System.out.println(" ║             상품별VIP고객조회       ");
					System.out.println("  =====================KOSTACO====");
					System.out.println("특정상품을 가장 많이 구입한 고객리스트를 불러옵니다.");
					System.out.println("===============================");
					System.out.print("특정 상품의 상품이름을 입력 =>[예 : 빼빼로]");
					itemName = sc.next();
					System.out.println("몇번째 고객까지 보시겠습니까 =>[예 : 5]");
					custCount = sc.nextInt();
					ArrayList<CustomerVO> list2 = customerDAO.findCustByItem(itemName, custCount);
					System.out.println("======가장 많이 구입 한 고객 리스트======");
					for(CustomerVO vo : list2) {
						System.out.println(index2 + " 고객번호 : " + vo.getCustId() + "| 고객이름 : " + vo.getCustName());
						index2++;
					}
					break;
					
			}
			break;
			
			
			case 3:
				//회원정보수정
				
				System.out.println("  =====================KOSTACO====");
				System.out.println(" ║             회원정보수정           ");
				System.out.println("  =====================KOSTACO====");
				System.out.println(" ║ 1. 고객 연락처 수정                ");
				System.out.println(" ║ 2. 고객 주소 수정                  ");
				System.out.println("  =====================KOSTACO====");
				System.out.print(" 임력 =>");
				int index3 = sc.nextInt();
				
				switch(index3) {
				case 1:
					//고객 번호를 받아 연락처 수정하기
					int custId;
					String custPhone;
					
					System.out.println("  =====================KOSTACO====");
					System.out.println(" ║             회원연락처수정         ");
					System.out.println("  =====================KOSTACO====");
					System.out.print("회원번호를 입력 =>");
					custId = sc.nextInt();
					System.out.println("변경된 연락처를 입력 =>");
					custPhone = sc.next();
					customerDAO.updateCustPhone(custId, custPhone);
					break;
				case 2:
					//고객 번호를 받아 주소 수정하기
					int custId2;
					String custAddr;
					
					System.out.println("  =====================KOSTACO====");
					System.out.println(" ║             회원연락처수정         ");
					System.out.println("  =====================KOSTACO====");
					System.out.print("회원번호를 입력 =>");
					custId2 = sc.nextInt();
					System.out.println("변경된 주소 입력 =>");
					custAddr = sc.next();
					customerDAO.updateCustPhone(custId2, custAddr);
					
					break;
				}
				
				break;
				
			case 4 : 
				
				//회원삭제
				String custName;
				String custPhone;
				System.out.println("  =====================KOSTACO====");
				System.out.println(" ║             회원삭제             ");
				System.out.println("  =====================KOSTACO====");
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
				
				
			case 0:
				//종료
			    System.out.println("프로그램을 종료합니다.");
			    return;
			}
		}
	}
}
