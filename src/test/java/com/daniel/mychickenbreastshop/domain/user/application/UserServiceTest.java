package com.daniel.mychickenbreastshop.domain.user.application;

import com.daniel.mychickenbreastshop.domain.user.mapper.UserDetailMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserJoinMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.UserModifyMapper;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.domain.user.redis.model.UserRedisEntity;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

    @InjectMocks
    private UserService userService;

    private List<User> users;
    private List<ListResponseDto> listResponseDtos;

    @BeforeEach
    void setUpUsers() {
        users = new ArrayList<>();
        listResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            User user = User.builder()
                    .id((long) i)
                    .loginId("id" + i)
                    .password("password")
                    .salt(PasswordEncrypt.getSalt())
                    .name("name" + i)
                    .email("email" + i + "@gogo.com")
                    .address("address" + i)
                    .zipcode("zipcode" + i)
                    .role(Role.ROLE_USER)
                    .build();
            user.create();
            users.add(user);

            ListResponseDto listResponseDto = ListResponseDto.builder()
                    .userId("" + i)
                    .loginId("id" + i)
                    .name("name" + i)
                    .email("email" + i + "@gogo.com")
                    .address("address" + i)
                    .zipcode("zipcode" + i)
                    .role(Role.ROLE_USER.getRoleName())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
            listResponseDtos.add(listResponseDto);
        }

    }

    @DisplayName("회원 번호로 회원을 조회한다.")
    @Test
    void getUserTest() {
        // given
        DetailResponseDto responseDto = DetailResponseDto.builder()
                .userId(1L)
                .loginId("loginId")
                .email("email@naver.com")
                .address("address")
                .zipcode("zipcode")
                .role(Role.ROLE_USER)
                .build();

        Optional<User> optionalUser = Optional.ofNullable(users.get(0));

        // when
        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
        when(userDetailMapper.toDTO(any(User.class))).thenReturn(responseDto);

        assertThat(userService.getUser(users.get(0).getId()).getUserId()).isEqualTo(1L);
    }

    @DisplayName("유저 목록을 반환한다.")
    @Test
    void getAllUsersTest() {
        // given
        int pageNumber = 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> page = new PageImpl<>(List.of(users.get(0), users.get(1), users.get(2), users.get(3), users.get(4),
                users.get(5), users.get(6), users.get(7), users.get(8), users.get(9)), pageRequest, 10);

        // when
        when(userRepository.findAll(pageRequest)).thenReturn(page);
        when(userListMapper.toDTO(any(User.class))).thenReturn(listResponseDtos.get(0));

        assertThat(userService.getAllUsers(pageNumber)).hasSize(10);
    }

    @DisplayName("검색 조건에 맞는 유저 목록을 반환한다.")
    @Test
    void searchUsersTest() {
        // given
        UserSearchDto userSearchDto = new UserSearchDto("loginId", "id");
        int pageNumber = 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> page = new PageImpl<>(List.of(users.get(0), users.get(1), users.get(2), users.get(3), users.get(4),
                users.get(5), users.get(6), users.get(7), users.get(8), users.get(9)), pageRequest, 10);
        Role role = Role.ROLE_USER;

        // when
        when(userRepository.findUserWithDynamicQuery(pageRequest, userSearchDto, role)).thenReturn(page);
        when(userListMapper.toDTO(any(User.class))).thenReturn(listResponseDtos.get(0));

        assertThat(userService.searchUsers(pageNumber, userSearchDto, role)).hasSize(10);
    }

    @DisplayName("회원 가입을 진행한다.")
    @Test
    void joinTest() {
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

        UserRedisEntity redisEntity = new UserRedisEntity(joinUser.getEmail(), String.valueOf(emailRandomKey));

        // when
        when(userRedisStore.getData(redisEntity.getEmail(), UserRedisEntity.class)).thenReturn(redisEntity);
        when(userJoinMapper.toEntity(any(JoinRequestDto.class))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.join(joinUser)).isEqualTo(1L);
    }

    @DisplayName("회원 가입 시 중복된 유저가 있을 경우 에외를 발생시킨다.")
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

        assertThatThrownBy(() -> userService.join(joinUser))
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

        UserRedisEntity redisEntity = new UserRedisEntity(joinUser.getEmail(), String.valueOf(emailRandomKey));

        // when
        when(userRedisStore.getData(redisEntity.getEmail(), UserRedisEntity.class)).thenReturn(redisEntity);

        assertThatThrownBy(() -> userService.join(joinUser))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("인증 번호가 일치하지 않습니다. 재인증 받아 주세요.");
    }
}
