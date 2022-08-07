package com.daniel.mychickenbreastshop.domain.user.domain;


import com.daniel.mychickenbreastshop.domain.user.enums.UserGrade;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String userId;

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
