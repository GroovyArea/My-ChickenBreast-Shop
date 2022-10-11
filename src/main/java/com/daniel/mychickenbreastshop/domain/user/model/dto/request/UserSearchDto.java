package com.daniel.mychickenbreastshop.domain.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {

    private String searchKey; // 아이디, 이름, 이메일
    private String searchValue;
}