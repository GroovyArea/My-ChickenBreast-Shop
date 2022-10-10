package com.daniel.mychickenbreastshop.domain.user.domain.query;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Querydsl 전용 인터페이스
 */
public interface UserCustomQueryRepository {

    Page<User> findUserWithDynamicQuery(Pageable pageable, UserSearchDto searchDto, Role role);
}
