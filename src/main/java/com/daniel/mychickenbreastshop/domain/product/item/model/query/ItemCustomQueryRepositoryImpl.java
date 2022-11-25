package com.daniel.mychickenbreastshop.domain.product.item.model.query;

import com.daniel.mychickenbreastshop.domain.product.category.model.QCategory;
import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.product.category.model.QCategory.category;
import static com.daniel.mychickenbreastshop.domain.product.item.model.QProduct.product;

@RequiredArgsConstructor
@Repository
public class ItemCustomQueryRepositoryImpl implements ItemCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findItemWithDynamicQuery(Pageable pageable, ItemSearchDto itemSearchDto, ChickenCategory category, ChickenStatus status) {
        List<Product> results = queryFactory.selectFrom(product)
                .leftJoin(product.category, QCategory.category)
                .where(
                        searchDtoEq(itemSearchDto),
                        categoryEq(category),
                        statusEq(status)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> count = queryFactory.selectFrom(product)
                .leftJoin(product.category, QCategory.category)
                .where(searchDtoEq(itemSearchDto), categoryEq(category), statusEq(status));

        return PageableExecutionUtils.getPage(results, pageable, () -> count.fetch().size());
    }

    @Override
    public Page<Product> findAllByCategoryName(ChickenCategory categoryName, Pageable pageable) {
        List<Product> results = queryFactory.selectFrom(product)
                .leftJoin(product.category, category)
                .where(categoryEq(categoryName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> count = queryFactory.selectFrom(product)
                .leftJoin(product.category, category)
                .where(categoryEq(categoryName));

        return PageableExecutionUtils.getPage(results, pageable, () -> count.fetch().size());
    }

    private BooleanExpression categoryEq(ChickenCategory categoryCond) {
        return categoryCond != null ? product.category.categoryName.eq(categoryCond) : null;
    }

    private BooleanExpression statusEq(ChickenStatus statusCond) {
        return statusCond != null ? product.status.eq(statusCond) : null;
    }

    private BooleanExpression searchDtoEq(ItemSearchDto searchDto) {
        if (searchDto.getSearchValue() != "" && searchDto.getSearchKey() != null) {
            if (searchDto.getSearchKey().equals("name")) {
                return product.name.startsWith(searchDto.getSearchValue());
            }
            throw new BadRequestException("유효하지 않은 검색 조건 : " + searchDto.getSearchKey());
        }
        throw new BadRequestException("검색 조건 파라미터를 확인해주세요.");
    }
}
