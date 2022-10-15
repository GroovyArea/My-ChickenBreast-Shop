package com.daniel.mychickenbreastshop.domain.user.application;

import com.daniel.mychickenbreastshop.domain.user.mapper.UserDetailMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserJoinMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserModifyMapper;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceLittleTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RedisStore userRedisStore;
    @Mock
    private UserJoinMapper userJoinMapper;
    @Mock
    private UserDetailMapper userDetailMapper;
    @Mock
    private UserListMapper userListMapper;
    @Mock
    private UserModifyMapper userModifyMapper;

    @BeforeEach
    void setUpUsers() {

    }

    @DisplayName("회원 번호로 회원을 조회한다.")
    @Test
    void getUserTest() {
        User user = User.builder()
                .id(1L)
                .loginId("loginId")
                .password("password")
                .salt(PasswordEncrypt.getSalt())
                .email("email@naver.com")
                .address("address")
                .zipcode("zipcode")
                .role(Role.ROLE_USER)
                .build();

        DetailResponseDto responseDto = DetailResponseDto.builder()
                .userId(1L)
                .loginId("loginId")
                .email("email@naver.com")
                .address("address")
                .zipcode("zipcode")
                .role(Role.ROLE_USER)
                .build();

        Optional<User> optionalUser = Optional.ofNullable(user);

        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
        when(userDetailMapper.toDTO(any(User.class))).thenReturn(responseDto);

        UserService userService = new UserService(userRepository, userRedisStore, userJoinMapper, userDetailMapper, userListMapper, userModifyMapper);

        assertThat(userService.getUser(user.getId()).getUserId()).isEqualTo(1L);
    }
}
