package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
public class UserApiController {

    private final UserService userService;

    /**
     * 회원 디테일 조회
     *
     * @return 회원 정보
     */
    @GetMapping("/v1/users/{userId}")
    public ResponseEntity<DetailResponseDto> getUserDetail(@PathVariable Long userId) {
        DetailResponseDto userDTO = userService.getUser(userId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userDTO);
    }

    /**
     * 회원 수정
     *
     * @param modifyDTO 회원 수정 정보
     */
    @PatchMapping("/v1/users")
    public ResponseEntity<Void> modifyUser(@Valid @RequestBody ModifyRequestDto modifyDTO) {
        Long userId = getUserId();
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
     * @param page page 번호
     * @return 회원 리스트
     */
    @GetMapping("/v2/users")
    public ResponseEntity<List<ListResponseDto>> getAll(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(userService.getAllUsers(page));
    }

    /**
     * 관리자용 회원 검색 (이름, 이메일, 아이디, 회원 등급)
     *
     * @param page        페이지 번호
     * @param role        회원 등급
     * @param searchKey   검색 조건
     * @param searchValue 검색 값
     * @return 회원 검색 리스트
     */
    @GetMapping("/v2/users/search/{role}")
    public ResponseEntity<List<ListResponseDto>> searchUsers(
            @PathVariable Role role,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "name") String searchKey,
            @RequestParam(defaultValue = "") String searchValue) {

        return ResponseEntity.ok(userService.searchUsers(page, role, searchKey, searchValue));
    }

    private Long getUserId() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalDetails.getId();
    }
}
