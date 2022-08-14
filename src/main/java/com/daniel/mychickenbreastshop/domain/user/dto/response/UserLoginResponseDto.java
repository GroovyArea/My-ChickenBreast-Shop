package com.daniel.mychickenbreastshop.domain.user.dto.response;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserLoginResponseDto {

    private final Long id;
    private final List<String> roles;
    private final LocalDateTime createdTime;

    public UserLoginResponseDto(User user) {
        this.id = user.getId();
        this.roles = user.getRoles();
        this.createdTime = user.getCreatedAt();
    }
}
