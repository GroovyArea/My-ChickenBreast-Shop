package com.daniel.mychickenbreastshop.domain.user.service;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.dto.UserDTO;
import com.daniel.mychickenbreastshop.domain.user.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.domain.user.dto.request.LoginRequestDto;
import com.daniel.mychickenbreastshop.domain.user.dto.response.LoginResponseDto;
import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessages;
import com.daniel.mychickenbreastshop.domain.user.error.exception.UserExistException;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.JoinObjectMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.LoginResponseObjectMapper;
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
    private final LoginResponseObjectMapper loginResponseObjectMapper;

    @Transactional(readOnly = true)
    public UserDTO getUser(String userId) {
        return userObjectMapper.toDTO(userRepository.findByLoginId(userId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다.")));
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {
        validateDuplicatedUser(joinRequestDto.getLoginId());

        String salt = PasswordEncrypt.getSalt();
        joinRequestDto.setSalt(salt);

        try {
            joinRequestDto.setPassword(PasswordEncrypt.getSecurePassword(joinRequestDto.getPassword(), salt));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        return userRepository.save(joinObjectMapper.toEntity(joinRequestDto)).getId();
    }

    private void validateDuplicatedUser(String userId) {
        if (userRepository.findByLoginId(userId).isPresent()) {
            throw new UserExistException();
        }
    }

    public LoginResponseDto login(LoginRequestDto userLoginDTO) {
        User authUser = userRepository.findByLoginId(userLoginDTO.getLoginId()).orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_EXISTS_MESSAGE.getMessage()));
        return loginResponseObjectMapper.toDTO(authUser);
    }

}
