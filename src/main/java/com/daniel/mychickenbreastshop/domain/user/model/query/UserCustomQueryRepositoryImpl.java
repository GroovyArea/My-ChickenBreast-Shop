package com.daniel.mychickenbreastshop.domain.user.model.query;


import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.user.application.dto.request.UserSearchDto;
import com.daniel.ddd.user.domain.User;
import com.daniel.ddd.user.domain.enums.Role;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserCustomQueryRepositoryImpl implements UserCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> findUserWithDynamicQuery(Pageable pageable, UserSearchDto searchDto, Role role) {
        List<User> results = queryFactory.selectFrom(user)
                .where(searchDtoEq(searchDto), roleEq(role))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.createdAt.desc())
                .fetch();

        JPAQuery<User> count = queryFactory.selectFrom(user)
                .where(searchDtoEq(searchDto), roleEq(role));

        return PageableExecutionUtils.getPage(results, pageable, () -> count.fetch().size());
    }

    private BooleanExpression roleEq(Role roleCond) {
        return roleCond != null ? user.role.eq(roleCond) : null;
    }

    private BooleanExpression searchDtoEq(UserSearchDto searchDto) {
        if (!searchDto.getSearchValue().isBlank() && searchDto.getSearchKey() != null) {
            return switch (searchDto.getSearchKey()) {
                case "loginId" -> user.loginId.startsWith(searchDto.getSearchValue());
                case "name" -> user.name.startsWith(searchDto.getSearchValue());
                case "email" -> user.email.startsWith(searchDto.getSearchValue());
                default -> throw new BadRequestException("유효하지 않은 검색 조건 : " + searchDto.getSearchKey());
            };
        }
        throw new BadRequestException("검색 조건 파라미터를 확인해주세요.");
    }

}
