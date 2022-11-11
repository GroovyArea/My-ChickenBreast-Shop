package com.daniel.mychickenbreastshop.domain.product.application.file;

import com.daniel.mychickenbreastshop.domain.product.item.application.file.FileManager;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class FileManagerTest {

    private FileManager fileManager;

    @BeforeEach
    void setUp() {
        fileManager = new FileManager();
        ReflectionTestUtils.setField(fileManager, "uploadDirectory", "C:\\MyProject\\chickenBreastImages\\uploadImages");

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @DisplayName("지정한 경로에 파일을 업로드하고 업로드 파일 이름을 반환한다.")
    @Test
    void uploadFile() {
        // given
        String imageFileName = "image.png";

        MockMultipartFile multipartFile = new MockMultipartFile("image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        assertThat(fileManager.uploadFile(multipartFile)).contains(imageFileName);
    }

    @DisplayName("업로드 파일 삭제 실패 시 예외를 발생시킨다.")
    @Test
    void deleteFile() {
        // given
        String imageFileName = "image.png";

        assertThatThrownBy(() -> fileManager.deleteFile(imageFileName))
                .isInstanceOf(IllegalStateException.class); // 파일을 업로드 하지 않고 삭제할 경우
    }

    @DisplayName("이미지 파일의 리소스가 없을 경우 예외를 발생시킨다.")
    @Test
    void loadFile() {
        // given
        String imageFileName = "image.png";

        assertThatThrownBy(() -> fileManager.loadFile(imageFileName)) // 파일을 업로드 하지 않은 상태
                .isInstanceOf(BadRequestException.class).hasMessage("파일을 찾을 수 없습니다.");
    }

    @DisplayName("파일을 다운로드 할 수 있는 URI를 반환한다.")
    @Test
    void getDownloadURI() {
        // given
        String uploadFileName = UUID.randomUUID() + "_" + "image.png";

        // when
        String downloadURI = fileManager.getDownloadURI(uploadFileName);

        assertThat(downloadURI).contains("/api/v1/products/download/");
    }
}