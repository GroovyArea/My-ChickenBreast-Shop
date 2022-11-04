package com.daniel.mychickenbreastshop.domain.user.application;

import com.daniel.mychickenbreastshop.domain.user.mapper.UserDetailMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserJoinMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserModifyMapper;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.domain.user.model.enums.UserResponse;
import com.daniel.mychickenbreastshop.domain.user.redis.model.UserRedisEntity;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RedisStore userRedisStore;
    private final UserJoinMapper userJoinMapper;
    private final UserDetailMapper userDetailMapper;
    private final UserListMapper userListMapper;
    private final UserModifyMapper userModifyMapper;

    public DetailResponseDto getUser(Long userId) {
        return userDetailMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(UserResponse.USER_NOT_EXISTS.getMessage())));
    }

    public List<ListResponseDto> getAllUsers(int page) {
        PageRequest pageRequest = createPageRequest(page);
        List<User> users = userRepository.findAll(pageRequest).getContent();

        return users.stream()
                .map(userListMapper::toDTO)
                .toList();
    }
    public List<ListResponseDto> searchUsers(int page, Role role, String searchKey, String searchValue) {
        PageRequest pageRequest = createPageRequest(page);
        UserSearchDto searchDto = UserSearchDto.builder().searchValue(searchKey).searchValue(searchValue).build();
        List<User> searchedUsers = userRepository.findUserWithDynamicQuery(pageRequest, searchDto, role).getContent();

        return searchedUsers.stream()
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
        String savedKey = userRedisStore.getData(email, UserRedisEntity.class).getEmailRandomKey();

        if (savedKey == null) {
            throw new BadRequestException(UserResponse.MAIL_KEY_EXPIRED.getMessage());
        }

        if (!savedKey.equals(emailKey)) {
            throw new BadRequestException(UserResponse.MAIL_KEY_MISMATCH.getMessage());
        }
    }

    private PageRequest createPageRequest(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
