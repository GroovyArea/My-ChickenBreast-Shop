package com.daniel.mychickenbreastshop.user.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.application.port.in.JoinUseCase;
import com.daniel.mychickenbreastshop.user.application.service.redis.model.EmailRedisModel;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.mapper.UserJoinMapper;
import com.daniel.mychickenbreastshop.user.model.dto.request.JoinRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JoinServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisStore userRedisStore;

    @Mock
    private UserJoinMapper userJoinMapper;

    @InjectMocks
    private JoinUseCase joinService;

    @DisplayName("회원 가입을 진행한다.")
    @Test
    void join() {
        // given
        JoinRequestDto joinUser = JoinRequestDto.builder()
                .loginId("loginId")
                .password("password")
                .name("name")
                .email("email@naver.com")
                .emailAuthKey(String.valueOf(123456))
                .role(Role.ROLE_USER)
                .build();

        User user = User.builder()
                .id(1L)
                .loginId("loginId")
                .password("password")
                .name("name")
                .email("email@naver.com")
                .role(Role.ROLE_USER)
                .build();

        int emailRandomKey = 123456;

        EmailRedisModel emailRedisModel = new EmailRedisModel(joinUser.getEmail(), String.valueOf(emailRandomKey));

        // when
        when(userRedisStore.getData(emailRedisModel.getEmail(), EmailRedisModel.class).get()).thenReturn(emailRedisModel);
        when(userJoinMapper.toEntity(any(JoinRequestDto.class))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        Assertions.assertThat(joinService.join(joinUser)).isEqualTo(1L);
    }

    @DisplayName("회원 가입 시 중복된 유저가 있을 경우 예외를 발생시킨다.")
    @Test
    void duplicatedUserTest() {
        // given
        JoinRequestDto joinUser = JoinRequestDto.builder()
                .loginId("loginId")
                .password("password")
                .name("name")
                .email("email@naver.com")
                .emailAuthKey(String.valueOf(123456))
                .role(Role.ROLE_USER)
                .build();

        // when
        when(userRepository.existsByLoginId(joinUser.getLoginId())).thenReturn(true); // 중복된 회원이 있을 경우.

        assertThatThrownBy(() -> joinService.join(joinUser))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("이미 동일 회원 정보가 존재합니다.");
    }
    
    @DisplayName("회원 가입 시 이메일 인증 번호가 틀릴 경우 예외를 발생시킨다.")
    @Test
    void emailKeyCheckTest() {
        // given
        JoinRequestDto joinUser = JoinRequestDto.builder()
                .loginId("loginId")
                .password("password")
                .name("name")
                .email("email@naver.com")
                .emailAuthKey(String.valueOf(333333))
                .role(Role.ROLE_USER)
                .build();

        int emailRandomKey = 123456;

        EmailRedisModel emailRedisModel = new EmailRedisModel(joinUser.getEmail(), String.valueOf(emailRandomKey));

        // when
        when(userRedisStore.getData(emailRedisModel.getEmail(), EmailRedisModel.class).get()).thenReturn(emailRedisModel);

        assertThatThrownBy(() -> joinService.join(joinUser))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("인증 번호가 일치하지 않습니다. 재인증 받아 주세요.");
    }

}