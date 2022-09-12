package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 회원 컨트롤러
 *
 * <pre>
 *     <b>history</b>
 *     1.0 2022.08.22 최초 작성
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원 디테일 조회
     *
     * @return 회원 정보
     */
    @GetMapping("/v1/users")
    public ResponseEntity<DetailResponseDto> getUserDetail(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        DetailResponseDto userDTO = userService.getUser(userId);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 회원 수정
     *
     * @param modifyDTO 회원 수정 정보
     */
    @PatchMapping("/v1/users")
    public ResponseEntity<Void> modifyUser(HttpServletRequest request,
                                           @Valid @RequestBody ModifyRequestDto modifyDTO) {
        Long userId = (Long) request.getAttribute("userId");
        userService.modifyUser(userId, modifyDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 삭제 처리
     *
     * @param userId 회원 고유 번호
     */
    @DeleteMapping("/v2/users/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 리스트 조회
     *
     * @param pageable page 객체
     * @return 회원 리스트
     */
    @GetMapping("/v1/users")
    public ResponseEntity<List<ListResponseDto>> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUser(pageable));
    }

    /**
     * 회원 검색
     *
     * @param loginId  로그인 아이디
     * @param pageable page 객체
     * @return 검색 회원 리스트
     */
    @GetMapping("/v1/users/search/{loginId}")
    public ResponseEntity<List<ListResponseDto>> searchUserByLoginId(
            @PathVariable String loginId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.searchUser(loginId, pageable));
    }
}
