# My-ChickenBreast-Shop
shop api with spring boot 

## ERD Model
<img src ='https://user-images.githubusercontent.com/89288109/183050194-77b631dc-d79f-4172-b8e4-af1aa57b534b.png'>

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
  
#### Order
- PK 
  - 컬럼명 : tid 주문 고유 번호 
- FK 
  - 컬럼명 : user_id 유저 아이디
  
  
#### CardInfo
- PK 
  - 컬럼명 : id Auto_Increment 사용
- FK 
  - 컬럼명 : tid 주문 고유 번호
- FK 
  - 컬럼명 : payment_id 결제 고유 번호
  
  
#### Amount
- PK 
  - 컬럼명 : id Auto_Increment 사용
- FK 
  - 컬럼명 : tid 주문 고유 번호
- FK 
  - 컬럼명 : payment_id 결제 고유 번호
  
#### Outbox-email
- PK 
  - 컬럼명 : id Auto_Increment 사용
- Outbox 패턴 사용 테이블 -> 이메일 전송 시 사용

#### Outbox-order
- PK 
  - 컬럼명 : id Auto_Increment 사용
- Outbox 패턴 사용 테이블 -> 주문 재고 파악 시 사용

