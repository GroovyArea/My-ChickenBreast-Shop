# My-ChickenBreast-Shop


## 프로젝트 개요
닭가슴살 주문, 구매 API 입니다.  
단순 기능 구현보다 `객체지향`을 공부하며 이를 최대한 지키는 코드 구성으로 작성하려 시작했습니다.  
상대적으로 빠르고 가벼운 단위 테스트 작성을 통해 메소드의 안정성을 높이는 방식을 해결했습니다.

<br>

## 프로젝트 기간
- 2022.04.28 ~ 

<br>

## 사용 기술 스택
```
Java 17, Gradle
Spring Boot 2.7.3
Spring data JPA
Spring Security
Mysql 8.0.17
Redis
```

<br>

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

</div>
</details>
