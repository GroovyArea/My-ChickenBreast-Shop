package com.daniel.mychickenbreastshop.domain.user.service;

import com.daniel.mychickenbreastshop.global.auth.security.error.exception.LoginFailedException;
import com.daniel.mychickenbreastshop.domain.user.domain.RoleType;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.dto.UserDTO;
import com.daniel.mychickenbreastshop.domain.user.dto.UserLoginDTO;
import com.daniel.mychickenbreastshop.domain.user.dto.request.UserJoinDto;
import com.daniel.mychickenbreastshop.domain.user.dto.response.UserLoginResponseDto;
import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessages;
import com.daniel.mychickenbreastshop.domain.user.error.exception.UserExistException;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.JoinObjectMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.UserObjectMapper;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

/**
 * User Sevice 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.03 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JoinObjectMapper joinObjectMapper;
    private final UserObjectMapper userObjectMapper;

    @Transactional(readOnly = true)
    public UserDTO getUser(String userId) {
        return userObjectMapper.toDTO(userRepository.findByLoginId(userId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다.")));
    }

    @Transactional
    public Long signUp(UserJoinDto userJoinDto) {
        validateDuplicatedUser(userJoinDto.getLoginId());

        String salt = PasswordEncrypt.getSalt();
        userJoinDto.setSalt(salt);

        try {
            userJoinDto.setPassword(PasswordEncrypt.getSecurePassword(userJoinDto.getPassword(), salt));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        return userRepository.save(joinObjectMapper.toEntity(userJoinDto)).getId();
    }

    private void validateDuplicatedUser(String userId) {
        if (userRepository.findByLoginId(userId).isPresent()) {
            throw new UserExistException();
        }
    }

    public UserLoginResponseDto login(UserLoginDTO userLoginDTO) {
        User authUser = userRepository.findByLoginId(userLoginDTO.getLoginId()).orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_EXISTS_MESSAGE.getMessage()));

        if (authUser.getRoles().contains(RoleType.WITHDRAWAL_USER.name())) {
            throw new RuntimeException(ResponseMessages.WITHDRAW_USER_MESSAGE.getMessage());
        }

        try {
            String dbSalt = authUser.getSalt();
            String loginPassword = PasswordEncrypt.getSecurePassword(userLoginDTO.getPassword(), dbSalt);
            String dbPassword = authUser.getPassword();

            if (!loginPassword.equals(dbPassword)) {
                throw new LoginFailedException(ResponseMessages.LOGIN_FAIL_MESSAGE.getMessage());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        return new UserLoginResponseDto(authUser);
    }

}
