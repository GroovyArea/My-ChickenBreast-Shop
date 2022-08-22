package com.daniel.mychickenbreastshop.domain.user.service;

import com.daniel.mychickenbreastshop.domain.user.domain.Role;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.UserResponse;
import com.daniel.mychickenbreastshop.domain.user.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.domain.user.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.dto.response.DetailDto;
import com.daniel.mychickenbreastshop.domain.user.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.DetailObjectMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.JoinObjectMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.ListObjectMapper;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

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
    private final DetailObjectMapper detailObjectMapper;
    private final ListObjectMapper listObjectMapper;

    @Transactional(readOnly = true)
    public DetailDto getUser(Long userId) {
        return detailObjectMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(UserResponse.USER_NOT_EXISTS.getMessage())));
    }

    @Transactional(readOnly = true)
    public List<ListResponseDto> getAllUser(Pageable pageable) {
        List<User> allList = userRepository.findAll(pageable).getContent();

        return allList.stream()
                .map(listObjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ListResponseDto> searchUser(String loginId, Pageable pageable) {
        List<User> searchList = userRepository.findByLoginIdContaining(loginId, pageable);

        return searchList.stream()
                .map(listObjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {
        checkDuplicatedUser(joinRequestDto.getLoginId());

        String salt = PasswordEncrypt.getSalt();
        joinRequestDto.setSalt(salt);

        try {
            joinRequestDto.setPassword(PasswordEncrypt.getSecurePassword(joinRequestDto.getPassword(), salt));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        joinRequestDto.setRole(Role.ROLE_USER);

        return userRepository.save(joinObjectMapper.toEntity(joinRequestDto)).getId();
    }

    @Transactional
    public void modifyUser(Long userId, ModifyRequestDto modifyDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(UserResponse.USER_NOT_EXISTS.getMessage()));

        user.update(modifyDTO, user.getSalt());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getLoginId(), user.getPassword(), SecurityContextHolder.getContext().getAuthentication().getAuthorities())
        );
    }

    @Transactional
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(UserResponse.USER_NOT_EXISTS.getMessage()));

        user.remove(Role.ROLE_WITHDRAWAL);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getLoginId(), user.getPassword(), SecurityContextHolder.getContext().getAuthentication().getAuthorities())
        );
    }

    private void checkDuplicatedUser(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new RuntimeException(UserResponse.USER_EXISTS.getMessage());
        }
    }

}
