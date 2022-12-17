package com.daniel.mychickenbreastshop.product.item.application.service;

import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ItemSearchUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.product.item.mapper.ItemDetailMapper;
import com.daniel.mychickenbreastshop.product.item.mapper.ItemListMapper;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemSearchServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ItemDetailMapper itemDetailMapper;

    @Mock
    private ItemListMapper itemListMapper;

    @InjectMocks
    private ItemSearchUseCase itemSearchService;

    private List<Product> products;
    private List<ListResponseDto> listResponseDtos;
    private Category category;

    @BeforeEach
    void setUpItems() {
        products = new ArrayList<>();
        listResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            Product product = Product.builder()
                    .id((long) i)
                    .name("name" + i)
                    .price(i * 1000)
                    .quantity(i * 100)
                    .image("image" + i)
                    .content("content" + i)
                    .status(ChickenStatus.SALE)
                    .build();

            product.updateCategoryInfo(category);

            products.add(product);

            ListResponseDto listResponseDto = ListResponseDto.builder()
                    .id(i)
                    .name("name" + i)
                    .price(i * 1000)
                    .quantity(i * 100)
                    .image("image" + i)
                    .status(ChickenStatus.SALE)
                    .category(category.getCategoryName())
                    .build();

            listResponseDtos.add(listResponseDto);
        }

        category = Category.builder()
                .id(1L)
                .categoryName(ChickenCategory.STEAMED)
                .products(new ArrayList<>())
                .build();
    }

    @DisplayName("상품 상세 정보를 조회한다.")
    @Test
    void getProduct() {
        // given
        DetailResponseDto detailResponseDto = DetailResponseDto.builder()
                .id(1)
                .name("name1")
                .price(10000)
                .quantity(100)
                .image("upload_image_url")
                .content("content1")
                .categoryName(ChickenCategory.STEAMED.STEAMED)
                .build();

        Optional<Product> optionalProduct = Optional.ofNullable(products.get(0));

        // when
        when(productRepository.findById(products.get(0).getId())).thenReturn(optionalProduct);
        when(itemDetailMapper.toDTO(products.get(0))).thenReturn(detailResponseDto);

        assertThat(itemSearchService.getProduct(products.get(0).getId())).isEqualTo(detailResponseDto);
    }

    @DisplayName("상품 카테고리에 해당하는 상품 목록을 조회한다.")
    @Test
    void getAllProducts() {
        // given
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(products.get(0), products.get(1), products.get(2), products.get(3), products.get(4),
                products.get(5), products.get(6), products.get(7), products.get(8), products.get(9)), pageable, 10);

        // when
        when(productRepository.findAllByCategoryName(category.getCategoryName(), pageable))
                .thenReturn(page);
        when(itemListMapper.toDTO(any(Product.class))).thenReturn(listResponseDtos.get(0));

        assertThat(itemSearchService.getAllProducts(category.getCategoryName(), pageable))
                .hasSize(10);
    }

    @DisplayName("검색 조건에 맞는 상품 목록을 반환한다.")
    @Test
    void searchProducts() {
        // given
        ItemSearchDto itemSearchDto = new ItemSearchDto("name", "name");
        int pageNumber = 1;
        ChickenStatus status = ChickenStatus.SALE;
        ChickenCategory categoryName = category.getCategoryName();

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(products.get(0), products.get(1), products.get(2), products.get(3), products.get(4),
                products.get(5), products.get(6), products.get(7), products.get(8), products.get(9)), pageable, 10);

        // when
        when(productRepository.findItemWithDynamicQuery(pageable, itemSearchDto, categoryName, status)).thenReturn(page);
        when(itemListMapper.toDTO(any(Product.class))).thenReturn(listResponseDtos.get(0));

        List<ListResponseDto> listResponseDtos = itemSearchService.searchProducts(pageable, status, categoryName, itemSearchDto);

        assertThat(listResponseDtos).hasSize(10);
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getCategory()).isEqualTo(category));
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getStatus()).isEqualTo(status));
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getName()).contains("name"));
    }

}