package com.daniel.mychickenbreastshop.domain.user.application;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.domain.model.Role;
import com.daniel.mychickenbreastshop.domain.user.domain.model.UserResponse;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserDetailMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserJoinMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserModifyMapper;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.store.RedisStore;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * User Service 클래스
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
    private final RedisStore redisStore;
    private final UserJoinMapper userJoinMapper;
    private final UserDetailMapper userDetailMapper;
    private final UserListMapper userListMapper;
    private final UserModifyMapper userModifyMapper;

    @Transactional(readOnly = true)
    public DetailResponseDto getUser(Long userId) {
        return userDetailMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(UserResponse.USER_NOT_EXISTS.getMessage())));
    }

    @Transactional(readOnly = true)
    public List<ListResponseDto> getAllUser(Pageable pageable) {
        List<User> allList = userRepository.findAll(pageable).getContent();

        return allList.stream()
                .map(userListMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ListResponseDto> searchUser(String loginId, Pageable pageable) {
        List<User> searchList = userRepository.searchByLoginId(loginId, pageable);

        return searchList.stream()
                .map(userListMapper::toDTO)
                .toList();
    }

    @Transactional
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

    @Transactional
    public void modifyUser(Long userId, ModifyRequestDto modifyDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(UserResponse.USER_NOT_EXISTS.getMessage()));

        String updatedPassword;
        try {
            updatedPassword = PasswordEncrypt.getSecurePassword(modifyDTO.getPassword(), user.getSalt());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        User modifier = userModifyMapper.toEntity(modifyDTO);

        user.updateUserInfo(modifier, updatedPassword);
    }

    @Transactional
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(UserResponse.USER_NOT_EXISTS.getMessage()));

        user.remove();
    }

    private void checkDuplicatedUser(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new BadRequestException(UserResponse.USER_EXISTS.getMessage());
        }
    }

    private void validateAuthKey(String email, String emailKey) {
        String savedKey = redisStore.getData(email);

        if (savedKey == null) {
            throw new BadRequestException(UserResponse.MAIL_KEY_EXPIRED.getMessage());
        }

        if (!savedKey.equals(emailKey)) {
            throw new BadRequestException(UserResponse.MAIL_KEY_MISMATCH.getMessage());
        }
    }

}
