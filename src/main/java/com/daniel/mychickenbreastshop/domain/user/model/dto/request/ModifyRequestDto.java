package com.daniel.mychickenbreastshop.domain.user.model.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ModifyRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "필수 입력 값입니다.")
    @Email(message = "적절한 이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "필수 입력 값입니다.")
    private String zipcode;
}
