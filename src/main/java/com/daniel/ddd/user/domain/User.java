package com.daniel.ddd.user.domain;


import com.daniel.ddd.order.domain.Order;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    private String password;

    private String salt;

    private String name;

    @Column(unique = true)
    private String email;

    private String address;

    private String zipcode;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 회원이 삭제 되어도 주문 데이터는 그냥 남아있어야 되기에, Cascade 사용 안함
    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<Order> orders = new ArrayList<>();


    // <비즈니스 로직 메서드> //

    public void updateUserInfo(final User modifiableEntity, final String updatePassword) {
        this.name = modifiableEntity.getName();
        this.email = modifiableEntity.getEmail();
        this.address = modifiableEntity.getAddress();
        this.zipcode = modifiableEntity.getZipcode();
        this.password = updatePassword;
    }

    public void remove() {
        this.role = Role.ROLE_WITHDRAWAL;
        this.delete();
    }

}
