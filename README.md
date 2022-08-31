# My-ChickenBreast-Shop


## 프로젝트 개요
닭가슴살 쇼핑몰 API 입니다.  
상품 조회, 검색 기능이 있습니다.  
Kakaopay REST API를 이용해 테스트 구매와 주문까지 가능하며,  
네이버 SMTP를 이용해 회원가입 유저는 이메일로 인증번호를 전송 받습니다.  

---

## 프로젝트 기간
- 2022.04.28 ~ 

---

## 사용 기술 스택
```
Java 17, Gradle
Spring Boot 2.7.3
Spring data JPA
Mysql 8.0.17
```

---

## ERD Model
<details>
<summary>▶</summary>
<div markdown="1">

<img src ='https://user-images.githubusercontent.com/89288109/183577933-57e41635-6535-4fdd-a3c1-e5a232d6afe5.png'>


### User
- PK 
  - 컬럼명 : id 유저 아이디
- Index 
  - 컬럼명 : phone 유저 휴대폰 번호 
  
### Product
- PK 
  - 컬럼명 : id 상품 번호 Auto_Increment 사용
- Index 
  - 컬럼명 : name 상품명
  
### Category
- PK 
  - 컬럼명 : id 카테고리 고유 번호 Auto_Increment 사용
  
### Order
- PK 
  - 컬럼명 : id 주문 고유 번호 Auto_Increment 사용
- FK 
  - 컬럼명 : user_id 유저 아이디
  
### Order_Product
- Pk
  - 컬럼명 : id 고유 번호 Auto_Increment 사용
- FK
  - 컬럼명 : order_id 주문 고유 번호
  
  
### CardInfo
- PK 
  - 컬럼명 : id 카드정보 고유 번호 Auto_Increment 사용
- FK 
  - 컬럼명 : order_id 주문 고유 번호
  - 컬럼명 : payment_id 결제 고유 번호
  
  
### Outbox-email
- PK 
  - 컬럼명 : id Auto_Increment 사용
- Outbox 패턴 사용 테이블 -> 이메일 전송 시 사용

### Outbox-order
- PK 
  - 컬럼명 : id Auto_Increment 사용
- Outbox 패턴 사용 테이블 -> 주문 재고 파악 시 사용

</div>
</details>
