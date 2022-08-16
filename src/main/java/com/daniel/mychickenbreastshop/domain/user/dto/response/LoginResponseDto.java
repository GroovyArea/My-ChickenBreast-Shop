package com.daniel.mychickenbreastshop.domain.user.dto.response;

import com.daniel.mychickenbreastshop.domain.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private Long id;
    private Role role;
    private LocalDateTime createdTime;

}
