package com.daniel.mychickenbreastshop.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {

    @Email(message = "형식에 맞는 이메일을 입력하세요.")
    private String email;
}
