
# 🏷️ KOSTA 주문/판매 관리 시스템

> **Java + Oracle 기반으로 구축한 주문 및 판매 통계 관리 프로젝트**  
> 교육기관 KOSTA에서 학습하며 실습한 백엔드 개발 프로젝트입니다.

---
## 👥 팀 소개

| 이름       | 역할     |
|------------|----------|
| 공우진     | 팀장     |
| 오창진     | 팀원     |
| 최완빈     | 팀원     |
| 김유진     | 팀원     |

> 💬 협업을 통해 문제 해결 능력과 SQL/Java 실무 역량을 함께 키워나간 팀입니다.
---
## 📚 개요

이 프로젝트는 고객, 상품, 주문 정보를 관리하고  
판매 실적과 순위를 조회할 수 있는  
**CUI 기반 Java 어플리케이션**입니다.

---

## 🎯 주요 기능

- 📦 **고객(Customer)**, **상품(Item)**, **주문(Order)** 등록 및 관리
- 📊 **월별/연도별/제조사별 판매 실적 통계** 출력
- 🖼️ **CUI 기반 사용자 인터페이스** 구현
- 🗂️ **DAO 패턴**을 활용한 객체지향적 DB 연동

---

## 🛠️ 사용 기술

| 구분        | 기술 내용                                      |
|-------------|-----------------------------------------------|
| Language    | Java                                           |
| DBMS        | Oracle (SQL Developer 사용)                    |
| 개발 도구   | Eclipse IDE, SQL Developer                                    |
| UI          | CUI                                            |
| DB 연동     | JDBC                                           |
| 구조        | DAO / VO / Run Class 구조화                   |
| 버전 관리   | Git, GitHub                                    |

---

## 🗃️ 테이블 구조 예시

### 📁 `item` 테이블

| 컬럼명       | 타입        | 설명           |
|--------------|-------------|----------------|
| item_id      | NUMBER      | 상품 ID (PK)   |
| name         | VARCHAR2    | 상품 이름      |
| maker        | VARCHAR2    | 제조사         |
| price        | NUMBER      | 가격           |

> ※ 제조사는 5개로 제한되어 목업 데이터 구성

---

## 🧪 목업 데이터

- `item`: 과자, 음료, 라면 등 다양한 품목 100개
- `customer`: 가상의 고객 30명
- `orders`, `orders_detail`: 실제 주문 데이터 예시 구성

> 📁 모든 데이터는 SQL Developer를 통해 수작업/스크립트로 삽입

---

## 🖼️ 화면 예시


![메인화면](https://github.com/user-attachments/assets/27e2ce67-80a6-457b-bee5-1b0253f02cb2)



---


## 📂 패키지 구성

```
src/
├── common
│   ├── ConnectionProvider.java
│   ├── CustomerRun.java
│   ├── ItemRun.java
│   ├── OrdersRun.java
│   ├── SalesStatsRun.java
│   ├── MainApp.java

├── dao/        
│   ├── OrdersDAO.java
│   ├── OrdersDetailDAO.java
│   ├── CustomerDAO.java
│   ├── SalesStatsDAO.java
│   ├── ItemDAO.java
├── vo/
│   ├── CustomerVO.java
│   ├── ItemVO.java
│   ├── OrdersVO.java
│   ├── OrdersDetailVO.java
└── └── SalesStatsVO.java
```

---

## ✍️ GitHub 활용 기록

- GitHub 초기화 및 README 생성
- Eclipse에서 프로젝트 관리
- `.gitignore`, `.md`, 버전 커밋 및 푸시 실습

---

## 📌 향후 발전 방향

- 관리자 페이지 구축
- 회원 로그인 기능 추가
- 웹 기반(Spring Boot 등) 확장

---

## 📮 문의

- 🙋‍♂️ 개발자: (이게왜안되조)
- 작성일: 2025년 4월

---

> 🚀 *KOSTA에서 배우고 직접 개발한 실습형 백엔드 프로젝트입니다.*
