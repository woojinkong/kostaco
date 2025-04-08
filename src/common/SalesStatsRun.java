package common;

import java.util.List;
import java.util.Scanner;

import dao.SalesStatsDAO;
import vo.SalesStatsVO;

public class SalesStatsRun {
	
	  public void salesStatsrun(Scanner sc) {
	        SalesStatsDAO dao = new SalesStatsDAO();

        while (true) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("       📊 KOSTACO 판매 실적 관리       ");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("1. 월별 총 판매 실적 조회");
            System.out.println("2. 연도별 총 판매 실적 조회");
            System.out.println("3. 제조사별 총 판매 실적 조회");
            System.out.println("4. 제조사별 월별 상품 판매 순위 조회");
            System.out.println("0. 종료");
            System.out.println("╠══════════════════════KOSTACO═════════╣");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine(); // 개행 처리

            switch (choice) {
                case 1:
                    System.out.println("\n📅 월별 총 판매 실적");
                    List<SalesStatsVO> monthlyStats = dao.getMonthlySales();
                    for (SalesStatsVO stat : monthlyStats) {
                        System.out.printf("월: %s | 총 매출: %,d원\n", stat.getPeriod(), stat.getTotalSales());
                    }
                    break;

                case 2:
                    System.out.println("\n📆 연도별 총 판매 실적");
                    List<SalesStatsVO> yearlyStats = dao.getYearlySales();
                    for (SalesStatsVO stat : yearlyStats) {
                        System.out.printf("연도: %s | 총 매출: %,d원\n", stat.getPeriod(), stat.getTotalSales());
                    }
                    break;

                case 3:
                    System.out.println("\n🏭 제조사별 총 판매 실적");
                    List<SalesStatsVO> makerStats = dao.getSalesStats();
                    for (SalesStatsVO stat : makerStats) {
                        System.out.printf("제조사: %s | 총 매출: %,d원\n", stat.getMaker(), stat.getTotalSales());
                    }
                    break;

                case 4:
                    System.out.println("\n🏅 제조사별 월별 상품 판매 순위");
                    List<SalesStatsVO> rankingStats = dao.getMakerMonthlyItemRankings();
                    for (SalesStatsVO stat : rankingStats) {
                        System.out.printf(
                            "제조사: %s | 월: %s | 순위: %d | 상품: %s | 수량: %,d개 | 매출: %,d원\n",
                            stat.getManufacturer(), stat.getPeriod(), stat.getRank(),
                            stat.getItemName(), stat.getQuantitySold(), stat.getTotalSales()
                        );
                    }
                    break;

                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    //sc.close();
                    return;

                default:
                    System.out.println("❗ 올바른 번호를 입력해주세요.");
            }
        }
    }
}

