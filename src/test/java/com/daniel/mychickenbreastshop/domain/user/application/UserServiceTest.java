package com.daniel.mychickenbreastshop.domain.user.application;

import com.daniel.mychickenbreastshop.ApplicationTest;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.domain.model.Role;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {UserService.class})
class UserServiceTest extends ApplicationTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUpUsers() {
        for (int i = 0; i < 100; i++) {
            User build = User.builder()
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

            build.create();
            userRepository.save(build);
        }
    }

    @DisplayName("유저를 찾는다.")
    @Test
    void getUserTest() {
        DetailResponseDto user = userService.getUser(1L);

        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("name1");
        assertThat(user.getLoginId()).isEqualTo("id1");
    }

    @DisplayName("유저 목록을 반환한다.")
    @Test
    void getAllUsersTest() {
        List<ListResponseDto> allUsers = userService.getAllUsers(1);

        assertThat(allUsers).hasSize(10);
    }

    @DisplayName("검색 조건에 맞는 유저 목록을 반환한다.")
    @Test
    void searchUsersTest() {
        // given
        UserSearchDto userSearchDto = new UserSearchDto("loginId", "id");
        int page = 1;
        Role role = Role.ROLE_USER;

        // when
        List<ListResponseDto> listResponseDtos = userService.searchUsers(page, userSearchDto, role);

        assertThat(listResponseDtos).hasSize(10);
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

        int emailRandomKey = 123456;
        redisStore.setDataExpire(joinUser.getEmail(), String.valueOf(emailRandomKey), 5 * 60 * 1000L);

        // when
        Long joinUserId = userService.join(joinUser);

        assertThat(joinUserId).isNotNull();
    }

    @DisplayName("회원 가입 시 중복된 유저가 있을 경우 예외를 발생시킨다.")
    @Test
    void duplicatedUserTest() {
        // given
        JoinRequestDto joinUser = JoinRequestDto.builder()
                .loginId("id1")
                .password("password")
                .name("name")
                .email("email@naver.com")
                .emailAuthKey(String.valueOf(123456))
                .role(Role.ROLE_USER)
                .build();

        int emailRandomKey = 123456;
        redisStore.setDataExpire(joinUser.getEmail(), String.valueOf(emailRandomKey), 5 * 60 * 1000L);

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
        redisStore.setDataExpire(joinUser.getEmail(), String.valueOf(emailRandomKey), 5 * 60 * 1000L);

        assertThatThrownBy(() -> userService.join(joinUser))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("인증 번호가 일치하지 않습니다. 재인증 받아 주세요.");
    }

    @DisplayName("회원 정보 수정을 진행한다.")
    @Test
    void modifyUserTest() {
        // given
        ModifyRequestDto requestDto = ModifyRequestDto.builder()
                .name("changedName")
                .email("changedEmail")
                .password("changedPassword")
                .address("changedAddress")
                .zipcode("changedZipcode")
                .build();

        // when
        userService.modifyUser(1L, requestDto);

        assertThat(userService.getUser(1L).getName()).isEqualTo(requestDto.getName());
        assertThat(userService.getUser(1L).getEmail()).isEqualTo(requestDto.getEmail());
    }

    @DisplayName("회원 등급을 탈퇴 회원으로 변경한다.")
    @Test
    void removeUserTest() {
        userService.removeUser(1L);

        assertThat(userService.getUser(1L).getRole()).isEqualTo(Role.ROLE_WITHDRAWAL);
    }
}