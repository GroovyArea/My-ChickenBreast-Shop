package com.daniel.mychickenbreastshop.domain.user.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증을 위한 Redis 저장 객체
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserStoreEntity {

    private String email;
    private String emailRandomKey;

}
