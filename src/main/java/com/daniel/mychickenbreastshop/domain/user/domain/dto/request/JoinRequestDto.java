package com.daniel.mychickenbreastshop.domain.user.domain.dto.request;

import com.daniel.mychickenbreastshop.domain.user.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class JoinRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private String loginId;

    @NotBlank(message = "필수 입력 값입니다.")
    @Setter
    private String password;

    @Setter
    private String salt;

    @NotBlank(message = "필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "필수 입력 값입니다.")
    @Email(message = "형식에 맞는 이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "필수 입력 값입니다.")
    private String zipcode;

    @Setter
    private Role role;

    @NotBlank(message = "필수 입력 값입니다.")
    private String emailAuthKey;

}
