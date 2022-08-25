package com.daniel.mychickenbreastshop.auth.security.application;

import com.daniel.mychickenbreastshop.auth.security.mapper.PrincipalDetailMapper;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.model.UserResponse;
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
    private final PrincipalDetailMapper principalDetailMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException(UserResponse.USER_NOT_EXISTS.getMessage()));
        return principalDetailMapper.toDTO(user);
    }
}
