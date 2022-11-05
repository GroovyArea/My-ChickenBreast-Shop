package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.domain.product.application.manage.FileManager;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemDetailMapper;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemListMapper;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemModifyMapper;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemRegisterMapper;
import com.daniel.mychickenbreastshop.domain.product.model.category.Category;
import com.daniel.mychickenbreastshop.domain.product.model.category.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.domain.product.model.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private FileManager fileManager;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ItemDetailMapper itemDetailMapper;

    @Mock
    private ItemListMapper itemListMapper;

    @Mock
    private ItemRegisterMapper itemRegisterMapper;

    @Mock
    private ItemModifyMapper itemModifyMapper;

    @InjectMocks
    private ProductService productService;

    private List<Product> products;
    private List<ListResponseDto> listResponseDtos;
    private Category category;

    @BeforeEach
    void setUpItems() {
        products = new ArrayList<>();
        listResponseDtos = new ArrayList<>();

        category = Category.builder()
                .id(1L)
                .categoryName(ChickenCategory.STEAMED)
                .products(new ArrayList<>())
                .build();

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
                    .id((long) i)
                    .name("name" + i)
                    .price(i * 1000)
                    .quantity(i * 100)
                    .image("image" + i)
                    .status(ChickenStatus.SALE)
                    .category(category.getCategoryName())
                    .build();

            listResponseDtos.add(listResponseDto);
        }
    }

    @DisplayName("상품 상세 정보를 조회한다.")
    @Test
    void getProduct() {
        // given
        String downLoadURI = "/api/v1/products/download/image1";

        DetailResponseDto detailResponseDto = DetailResponseDto.builder()
                .id(1)
                .name("name1")
                .price(1000)
                .quantity(100)
                .image(downLoadURI)
                .content("content1")
                .categoryName(ChickenCategory.STEAMED)
                .build();

        Optional<Product> optionalProduct = Optional.ofNullable(products.get(0));

        // when
        when(productRepository.findById(products.get(0).getId())).thenReturn(optionalProduct);
        when(fileManager.getDownloadURI(products.get(0).getImage())).thenReturn(downLoadURI);
        when(itemDetailMapper.toDTO(products.get(0))).thenReturn(detailResponseDto);

        assertThat(productService.getProduct(products.get(0).getId())).isEqualTo(detailResponseDto);
    }

    @DisplayName("상품 카테고리에 해당하는 상품 목록을 조회한다.")
    @Test
    void getAllProduct() {
        // given
        int pageNumber = 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(products.get(0), products.get(1), products.get(2), products.get(3), products.get(4),
                products.get(5), products.get(6), products.get(7), products.get(8), products.get(9)), pageRequest, 10);

        ChickenCategory category = ChickenCategory.STEAMED;

        // when
        when(productRepository.findAllByCategoryName(category, pageRequest)).thenReturn(page);
        when(itemListMapper.toDTO(any(Product.class))).thenReturn(listResponseDtos.get(0));

        assertThat(productService.getAllProduct(category, pageNumber)).hasSize(10);
    }

    @DisplayName("검색 조건에 맞는 상품 목록을 반환한다.")
    @Test
    void searchProducts() {
        // given
        ItemSearchDto itemSearchDto = new ItemSearchDto("name", "name");
        int pageNumber = 1;
        ChickenStatus status = ChickenStatus.SALE;
        ChickenCategory category = ChickenCategory.STEAMED;

        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(products.get(0), products.get(1), products.get(2), products.get(3), products.get(4),
                products.get(5), products.get(6), products.get(7), products.get(8), products.get(9)), pageRequest, 10);

        // when
        when(productRepository.findItemWithDynamicQuery(pageRequest, itemSearchDto, category, status)).thenReturn(page);
        when(itemListMapper.toDTO(any(Product.class))).thenReturn(listResponseDtos.get(0));

        List<ListResponseDto> listResponseDtos = productService.searchProducts(pageNumber, status, category, itemSearchDto);

        assertThat(listResponseDtos).hasSize(10);
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getCategory()).isEqualTo(category));
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getStatus()).isEqualTo(status));
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getName()).contains("name"));
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void registerItem() {
        // given
        String imageFileName = "image.png";
        MockMultipartFile multipartFile = new MockMultipartFile("image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        String uploadFilename = UUID.randomUUID() + "_" + imageFileName;

        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .name("name")
                .price(1000).quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        // when
        when(fileManager.uploadFile(multipartFile)).thenReturn(uploadFilename);
        when(categoryRepository.findByCategoryName(requestDto.getCategory())).thenReturn(Optional.ofNullable(category));
        when(productRepository.save(products.get(0))).thenReturn(products.get(0));
        when(itemRegisterMapper.toEntity(requestDto)).thenReturn(products.get(0));

        Long itemId = productService.registerItem(requestDto, multipartFile);

        assertThat(itemId).isEqualTo(1L);
    }

    @DisplayName("상품 정보를 수정한다.")
    @Test
    void modifyItem() {
        // given
        ModifyRequestDto modifyRequestDto = ModifyRequestDto.builder()
                .id(1L)
                .name("name")
                .price(1000).quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        String imageFileName = "modifiedImage.png";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        String uploadFilename = UUID.randomUUID() + "_" + imageFileName;

        Product modifiedProduct = Product.builder()
                .id(1L)
                .name("name")
                .price(1000).quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(category)
                .image("originalImage.png")
                .build();

        // when
        when(fileManager.uploadFile(multipartFile)).thenReturn(uploadFilename);
        when(categoryRepository.findByCategoryName(modifyRequestDto.getCategory())).thenReturn(Optional.ofNullable(category));
        when(productRepository.findById(modifyRequestDto.getId())).thenReturn(Optional.ofNullable(modifiedProduct));
        when(itemModifyMapper.toEntity(modifyRequestDto)).thenReturn(modifiedProduct);

        productService.modifyItem(modifyRequestDto, multipartFile);

        assertThat(modifiedProduct.getImage()).isEqualTo(uploadFilename);
    }

    @DisplayName("상품 상태를 단종으로 변경한다.")
    @Test
    void removeItem() {
        // given
        Long itemNo = 1L;

        // when
        when(productRepository.findById(itemNo)).thenReturn(Optional.ofNullable(products.get(0)));

        productService.removeItem(itemNo);

        assertThat(products.get(0).getStatus()).isEqualTo(ChickenStatus.EXTINCTION);
    }

    @DisplayName("결제할 상품 가격과 수량의 총 결제 금액의 유효성을 검사하며 불일치할 경우 예외를 발생시킨다.")
    @Test
    void validatePayAmount() {
        // given
        Long itemNo = 1L;
        int itemQuantity = 10;
        long totalPrice = 1000; // 원래 10,000원 이어야 함.

        // when
        when(productRepository.findById(itemNo)).thenReturn(Optional.ofNullable(products.get(0)));

        assertThatThrownBy(() -> productService.validatePayAmount(itemNo, itemQuantity, totalPrice))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("상품 총 가격이 잘못 되었습니다.");
    }
}
