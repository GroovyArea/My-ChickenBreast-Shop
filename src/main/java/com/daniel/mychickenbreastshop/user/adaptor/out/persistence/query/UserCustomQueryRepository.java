package com.daniel.mychickenbreastshop.user.adaptor.out.persistence.query;


import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Querydsl 전용 인터페이스
 */
public interface UserCustomQueryRepository {

    Page<User> findUserWithDynamicQuery(Pageable pageable, UserSearchDto searchDto, Role role);
}
