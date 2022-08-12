package com.daniel.mychickenbreastshop.domain.user.domain;


import com.daniel.mychickenbreastshop.domain.user.enums.UserGrade;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    private String password;

    private String salt;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String zipcode;

    @Enumerated(EnumType.STRING)
    private UserGrade grade;

}
