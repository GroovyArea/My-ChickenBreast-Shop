package com.daniel.mychickenbreastshop.user.domain;


import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import lombok.*;

import javax.persistence.*;

@Table(name = "users", indexes = {
        @Index(name = "idx__email__role", columnList = "email, role", unique = true),
        @Index(name = "idx__login_id__role", columnList = "loginId, role", unique = true),
        @Index(name = "idx__name__role", columnList = "name, role")
})
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    private String password;

    private String salt;

    private String name;

    @Column(nullable = false)
    private String email;

    private String address;

    private String zipcode;

    @Enumerated(EnumType.STRING)
    private Role role;

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
