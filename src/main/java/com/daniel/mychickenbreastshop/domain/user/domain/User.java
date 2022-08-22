package com.daniel.mychickenbreastshop.domain.user.domain;


import com.daniel.mychickenbreastshop.domain.user.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;

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

    public void update(final ModifyRequestDto modifyDTO, final String salt) {
        updateName(modifyDTO.getName());
        updateEmail(modifyDTO.getEmail());
        updateAddress(modifyDTO.getAddress());
        updateZipcode(modifyDTO.getZipcode());
        updatePassword(modifyDTO.getPassword(), salt);
    }

    public void remove(final Role role) {
        updateRole(role);
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

    private void updatePassword(final String password, final String salt) {
        try {
            this.password = PasswordEncrypt.getSecurePassword(password, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void updateRole(final Role role) {
        this.role = role;
    }

}
