# My-ChickenBreast-Shop


## 프로젝트 개요
닭가슴살 주문, 구매 API 입니다.  
단순 기능 구현보다 `객체지향`을 공부하며 이를 최대한 지키는 코드 구성에 포커스를 두고 시작했습니다.  
상대적으로 빠르고 가벼운 단위 테스트 작성을 통해 메소드의 안정성을 높이는 방식을 택했습니다.

<br>

## 프로젝트 기간
- 2022.04.28 ~ 

<br>

## 사용 기술 스택
- Java 17
- Spring Boot 2.7.3
- Spring data JPA, Querydsl
- Spring Security
- Mysql
- Redis

<br>

## ERD Model
<details>
<summary></summary>
<div markdown="1">

<img src ='https://user-images.githubusercontent.com/89288109/207262271-a33c50bc-6fa7-44fc-bccf-c9f26d0a2852.png'>

### User
- PK 
  - 컬럼명 : id 유저 아이디 Auto_Increment 사용
- Index 
  - 컬럼명 : login_id 유저 로그인 아이디 
  - 컬럼명 : email 유저 이메일
  - 컬럼명 : name 유저 이름 
  
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
- Index 
  - 컬럼명 : user_id 유저 아이디
  - 컬럼명 : payment_id 결제 건 아이디
  
### Order_Product
- Pk
  - 컬럼명 : id 고유 번호 Auto_Increment 사용
- FK
  - 컬럼명 : order_id 주문 고유 번호

### Payment
- PK
  - 컬럼명 : id 결제 고유 번호 Auto_Increment 사용
- Index
  - 컬럼명 : order_id 유저 아이디
- FK
  - 컬럼명 : card_id 카드 고유 아이디
  - 
### CardInfo
- PK 
  - 컬럼명 : id 카드정보 고유 번호 Auto_Increment 사용
- FK 
  - 컬럼명 : payment_id 결제 고유 번호

</div>
</details>
