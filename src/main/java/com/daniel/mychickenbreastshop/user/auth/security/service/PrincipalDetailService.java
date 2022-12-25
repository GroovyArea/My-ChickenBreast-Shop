package com.daniel.mychickenbreastshop.user.auth.security.service;

import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 토큰에 저장된 정보를 통해
 * 회원 정보를 조회하는 서비스 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.13 최초 작성
 *     1.1, 2022.08.16 수정(메서드 세부 내용)
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.USER_NOT_EXISTS.getMessage()));

        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user) {
        return PrincipalDetails.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
    }
}
