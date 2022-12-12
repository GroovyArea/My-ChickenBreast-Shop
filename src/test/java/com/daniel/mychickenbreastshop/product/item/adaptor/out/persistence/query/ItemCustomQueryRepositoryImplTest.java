package com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.global.config.QuerydslConfig;
import com.daniel.mychickenbreastshop.product.category.adaptor.out.persistence.CategoryRepository;
import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class ItemCustomQueryRepositoryImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Category category = Category.builder()
                .categoryName(ChickenCategory.STEAMED)
                .build();

        categoryRepository.save(category);

        for (int i = 1; i < 50; i++) {
            Product product = Product.builder()
                    .name("name" + i)
                    .content("content" + i)
                    .category(category)
                    .image("image" + i)
                    .price(10000)
                    .status(ChickenStatus.SALE)
                    .build();

            productRepository.save(product);
        }
    }

    @DisplayName("상품 검색 조건에 맞게 검색 결과 페이징 반환한다.")
    @Test
    void findItemWithDynamicQuery() {
        // given
        int page = 1;
        PageRequest pageRequest = PageRequest.of(
                page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        ItemSearchDto itemSearchDto = ItemSearchDto.builder()
                .searchKey("name")
                .searchValue("name")
                .build();

        ChickenCategory chickenCategory = ChickenCategory.STEAMED;
        ChickenStatus chickenStatus = ChickenStatus.SALE;

        // when
        Page<Product> searchPage = productRepository.findItemWithDynamicQuery(
                pageRequest, itemSearchDto, chickenCategory, chickenStatus);
        List<Product> content = searchPage.getContent();

        assertThat(content).hasSize(10);
        content.forEach(product -> assertThat(product.getName()).contains("name"));
        content.forEach(product -> assertThat(product.getStatus()).isEqualTo(chickenStatus));
        content.forEach(product -> assertThat(product.getCategory().getCategoryName())
                .isEqualTo(chickenCategory));
    }

    @DisplayName("해당 카테고리의 상품 목록을 페이징하여 반환한다.")
    @Test
    void findAllByCategoryName() {
        // given
        int page = 1;
        PageRequest pageRequest = PageRequest.of(
                page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        ChickenCategory chickenCategory = ChickenCategory.STEAMED;

        // when
        Page<Product> productPage = productRepository.findAllByCategoryName(
                chickenCategory, pageRequest);
        List<Product> content = productPage.getContent();

        assertThat(content).hasSize(10);
        content.forEach(product -> assertThat(product.getCategory().getCategoryName())
                .isEqualTo(chickenCategory));
    }
}