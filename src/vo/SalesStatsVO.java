package vo;

// 판매 통계 정보를 담는 VO 클래스
public class SalesStatsVO {
		private String month;
		private String year;
		private String manufacturer;
		private String itemName;
		private int totalSales;
		private int rank;
		
		// 기본 생성자
		public SalesStatsVO() {
		
		}

		// 전체 정보 초기화 생성자
		public SalesStatsVO(String month, String year, String manufacturer, String itemName, int totalSales,
				int rank) {
			super();
			this.month = month;
			this.year = year;
			this.manufacturer = manufacturer;
			this.itemName = itemName;
			this.totalSales = totalSales;
			this.rank = rank;
		}
		
		// 연도별 실적용 생성자
		public SalesStatsVO(String year, int totalSales) {
			this.year = year;
			this.totalSales = totalSales;
		}
		
		// 월별 제조사 실적용 생성자
		public SalesStatsVO(String month, String manufacturer, int totalSales) {
			this.month = month;
			this.manufacturer = manufacturer;
			this.totalSales = totalSales;
		}
		
		// 제조사별 월별 상품 판매 순위 생성자
		public SalesStatsVO(String month, String manufacturer, String itemName, int totalSales, int rank) {
		    this.month = month;
		    this.manufacturer = manufacturer;
		    this.itemName = itemName;
		    this.totalSales = totalSales;
		    this.rank = rank;
		}
		
		// Getter & Setter
		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public int getTotalSales() {
			return totalSales;
		}

		public void setTotalSales(int totalSales) {
			this.totalSales = totalSales;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}
		
		
		

}
