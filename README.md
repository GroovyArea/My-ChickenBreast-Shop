# My-ChickenBreast-Shop
shop api with spring boot 

## ERD Model
<img src ='https://user-images.githubusercontent.com/89288109/183428015-8b442fa6-f13e-4e5c-8950-fed22ed0e67d.png'>

### Table

#### User
- PK 
  - 컬럼명 : id 유저 아이디
- Index 
  - 컬럼명 : phone 유저 휴대폰 번호 
  
#### Product
- PK 
  - 컬럼명 : id 상품 번호 Auto_Increment 사용
- Index 
  - 컬럼명 : name 상품명
  
#### Category
- PK 
  - 컬럼명 : id 카테고리 고유 번호 Auto_Increment 사용
  
#### Order
- PK 
  - 컬럼명 : id 주문 고유 번호 Auto_Increment 사용
- FK 
  - 컬럼명 : user_id 유저 아이디
  
#### Order_Product
- Pk
  - 컬럼명 : id 고유 번호 Auto_Increment 사용
- FK
  - 컬럼명 : order_id 주문 고유 번호
  
  
#### CardInfo
- PK 
  - 컬럼명 : id 카드정보 고유 번호 Auto_Increment 사용
- FK 
  - 컬럼명 : order_id 주문 고유 번호
  - 컬럼명 : payment_id 결제 고유 번호
  
  
#### Outbox-email
- PK 
  - 컬럼명 : id Auto_Increment 사용
- Outbox 패턴 사용 테이블 -> 이메일 전송 시 사용

#### Outbox-order
- PK 
  - 컬럼명 : id Auto_Increment 사용
- Outbox 패턴 사용 테이블 -> 주문 재고 파악 시 사용

