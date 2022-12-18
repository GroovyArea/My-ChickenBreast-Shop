package com.daniel.mychickenbreastshop.user.application.service;

import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.mapper.UserDetailMapper;
import com.daniel.mychickenbreastshop.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSearchServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailMapper userDetailMapper;

    @Mock
    private UserListMapper userListMapper;

    @InjectMocks
    private UserSearchService userSearchService;

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
                    .userId(i)
                    .loginId("id" + i)
                    .name("name" + i)
                    .email("email" + i + "@gogo.com")
                    .address("address" + i)
                    .zipcode("zipcode" + i)
                    .role(Role.ROLE_USER)
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
            listResponseDtos.add(listResponseDto);
        }
    }

    @DisplayName("회원 번호로 회원을 조회한다.")
    @Test
    void getUser() {
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

        assertThat(userSearchService.getUser(users.get(0).getId()).getUserId()).isEqualTo(1L);
    }

    @DisplayName("등급에 맞는 유저 목록을 반환한다.")
    @Test
    void getAllUsers() {
        // given
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> page = new PageImpl<>(List.of(users.get(0), users.get(1), users.get(2), users.get(3), users.get(4),
                users.get(5), users.get(6), users.get(7), users.get(8), users.get(9)), pageable, 10);

        Role role = Role.ROLE_USER;

        // when
        when(userRepository.findAllByRole(pageable, role)).thenReturn(page);
        when(userListMapper.toDTO(any(User.class))).thenReturn(listResponseDtos.get(0));

        assertThat(userSearchService.getAllUsers(pageable, role)).hasSize(10);
    }

    @DisplayName("검색 조건에 맞는 유저 목록을 반환한다.")
    @Test
    void searchUsers() {
        // given
        UserSearchDto userSearchDto = new UserSearchDto("loginId", "id");
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> page = new PageImpl<>(List.of(users.get(0), users.get(1), users.get(2), users.get(3), users.get(4),
                users.get(5), users.get(6), users.get(7), users.get(8), users.get(9)), pageable, 10);
        Role role = Role.ROLE_USER;

        // when
        when(userRepository.findUserWithDynamicQuery(
                any(Pageable.class),
                any(UserSearchDto.class),
                any(Role.class))).thenReturn(page);
        when(userListMapper.toDTO(any(User.class)))
                .thenReturn(listResponseDtos.get(0));

        assertThat(userSearchService.searchUsers(pageable, role, userSearchDto)).hasSize(10);
    }
}