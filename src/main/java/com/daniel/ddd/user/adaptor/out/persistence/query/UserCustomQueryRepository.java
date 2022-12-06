package com.daniel.ddd.user.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Querydsl 전용 인터페이스
 */
public interface UserCustomQueryRepository {

    Page<User> findUserWithDynamicQuery(Pageable pageable, UserSearchDto searchDto, Role role);
}
