package com.daniel.mychickenbreastshop.user.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.global.config.QuerydslConfig;
import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class UserCustomQueryRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        for (int i = 1; i < 50; i++) {
            User user1 = User.builder()
                    .loginId("1loginId" + i)
                    .name("1name" + i)
                    .email("1email" + i)
                    .role(Role.ROLE_USER)
                    .build();

            User user2 = User.builder()
                    .loginId("2loginId" + i)
                    .name("2name" + i)
                    .email("2email" + i)
                    .role(Role.ROLE_WITHDRAWAL)
                    .build();

            User user3 = User.builder()
                    .loginId("3loginId" + i)
                    .name("3name" + i)
                    .email("3email" + i)
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        }
    }

    @DisplayName("회원 검색 조건에 맞는 검색 결과를 반환한다.")
    @Test
    void findUserWithDynamicQuery() {
        // given
        int page = 1;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        UserSearchDto searchDto = UserSearchDto.builder()
                .searchKey("name") // 이름 검색 조건
                .searchValue("1name")
                .build();

        Role role = Role.ROLE_USER;

        // when
        Page<User> searchPage = userRepository.findUserWithDynamicQuery(pageable, searchDto, role);
        List<User> content = searchPage.getContent();

        assertThat(content).hasSize(10);
        content.forEach(user -> assertThat(user.getName()).contains("1name"));
        content.forEach(user -> assertThat(user.getRole()).isEqualTo(role));
    }
}