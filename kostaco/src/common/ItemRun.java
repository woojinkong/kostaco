package common;


import java.util.List;
import java.util.Scanner;

import dao.ItemDAO;
import vo.ItemVO;

public class ItemRun {

    int itemId, itemQty, itemPrice;
    String itemName, itemManuf, itemPromo;

    public void itemRun(Scanner scanner) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(" ================================");
            System.out.println("║    🛒 KOSTACO 매장관리시스템 🛒    ");
            System.out.println(" ================================");
            System.out.println("  ================================");
            System.out.println(" ║ 1. 상품 등록                    ");
            System.out.println(" ║ 2. 상품 조회                    ");
            System.out.println(" ║ 3. 상품 전체 조회                ");
            System.out.println(" ║ 4. 상품 수정                    ");
            System.out.println(" ║ 5. 상품 삭제                    ");
            System.out.println(" ║ 0. 종료                        ");
            System.out.println("  =====================KOSTACO====");

            System.out.print("입력 : ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume the newline character

            ItemDAO itemDAO = new ItemDAO();
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
                    // 아래 코드에서 선택
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
                        if (itemVO == null) {
                            System.out.println("해당 이름의 제품이 없습니다.");
                        } else {

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
                    String name = sc.nextLine();
                    System.out.println("수량을 입력해주세요.");
                    int qty = sc.nextInt();
                    itemDAO.updateItem(name, qty);

                    break;

                case 5:
                    System.out.println("제품을 입력해주세요.");
                    itemDAO.deleteByName(sc.nextLine());
                    break;
    			case 0 :
    				System.out.println("프로그램을 종료합니다.");
    				return;

                // 추가적인 case들 구현...
                default:
                    System.out.println("잘못된 선택입니다. 다시 입력해 주세요.");
            }
        }
    }

    // 문자열이 정수인지 확인하는 메서드
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);  // 숫자라면 정수로 파싱 성공
            return true;
        } catch (NumberFormatException e) {
            return false;  // 숫자가 아니면 예외 발생
        }
    }
}