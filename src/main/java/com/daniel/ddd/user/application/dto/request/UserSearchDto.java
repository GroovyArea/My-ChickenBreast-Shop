package com.daniel.ddd.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearchDto {

    private String searchKey; // 아이디, 이름, 이메일
    private String searchValue;
}