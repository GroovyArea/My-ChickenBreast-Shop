package com.daniel.ddd.user.model.dto.request;


import com.daniel.ddd.user.domain.enums.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
