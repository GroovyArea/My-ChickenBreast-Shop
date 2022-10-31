package com.daniel.mychickenbreastshop.domain.user.model;


import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
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

    public void updateUserInfo(final User modifier, final String updatePassword) {
        updateName(modifier.getName());
        updateEmail(modifier.getEmail());
        updateAddress(modifier.getAddress());
        updateZipcode(modifier.getZipcode());
        updatePassword(updatePassword);
    }

    public void remove() {
        updateToWithDrawRole();
        this.delete();
    }

    private void updateName(final String name) {
        this.name = name;
    }

    private void updateEmail(final String email) {
        this.email = email;
    }

    private void updateAddress(final String address) {
        this.address = address;
    }

    private void updateZipcode(final String zipcode) {
        this.zipcode = zipcode;
    }

    private void updatePassword(final String updatePassword) {
        this.password = updatePassword;
    }

    private void updateToWithDrawRole() {
        this.role = Role.ROLE_WITHDRAWAL;
    }

}
