package com.daniel.ddd.user.application.service;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.global.redis.store.RedisStore;
import com.daniel.ddd.global.util.PasswordEncrypt;
import com.daniel.ddd.user.adaptor.out.persistence.UserRepository;
import com.daniel.ddd.user.model.dto.request.JoinRequestDto;
import com.daniel.ddd.user.application.port.in.JoinUseCase;
import com.daniel.ddd.user.application.service.redis.model.EmailRedisModel;
import com.daniel.ddd.user.domain.enums.ErrorMessages;
import com.daniel.ddd.user.domain.enums.Role;
import com.daniel.ddd.user.mapper.UserJoinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class JoinService implements JoinUseCase {

    private final UserRepository userRepository;
    private final RedisStore userRedisStore;
    private final UserJoinMapper userJoinMapper;

    @Override
    public Long join(JoinRequestDto joinRequestDto) {
        checkDuplicatedUser(joinRequestDto.getLoginId());

        validateAuthKey(joinRequestDto.getEmail(), joinRequestDto.getEmailAuthKey());

        String salt = PasswordEncrypt.getSalt();
        joinRequestDto.setSalt(salt);

        try {
            joinRequestDto.setPassword(PasswordEncrypt.getSecurePassword(joinRequestDto.getPassword(), salt));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        joinRequestDto.setRole(Role.ROLE_USER);

        return userRepository.save(userJoinMapper.toEntity(joinRequestDto)).getId();
    }

    private void checkDuplicatedUser(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new BadRequestException(ErrorMessages.USER_EXISTS.getMessage());
        }
    }

    private void validateAuthKey(String email, String emailKey) {
        String savedKey = userRedisStore.getData(email, EmailRedisModel.class).
                orElseThrow(() -> new BadRequestException(ErrorMessages.INVALID_EMAIL.getMessage()))
                .getEmailRandomKey();

        if (savedKey == null) {
            throw new BadRequestException(ErrorMessages.MAIL_KEY_EXPIRED.getMessage());
        }

        if (!savedKey.equals(emailKey)) {
            throw new BadRequestException(ErrorMessages.MAIL_KEY_MISMATCH.getMessage());
        }
    }
}
