package com.daniel.mychickenbreastshop.domain.admin.api;

import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자용 회원 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserAdminApiController {

    private final UserService userService;

    /**
     * 회원 리스트 조회
     *
     * @return 회원 리스트
     */
    @GetMapping
    public ResponseEntity<List<ListResponseDto>> getAll(@PageableDefault(
            page = 1,
            sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    /**
     * 관리자용 회원 검색 (이름, 이메일, 아이디, 회원 등급)
     *
     * @param role        회원 등급
     * @param searchKey   검색 조건
     * @param searchValue 검색 값
     * @return 회원 검색 리스트
     */
    @GetMapping("/search/{role}")
    public ResponseEntity<List<ListResponseDto>> searchUsers(
            @PathVariable Role role,
            @PageableDefault(page = 1, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable,
            @RequestParam(defaultValue = "name") String searchKey,
            @RequestParam(defaultValue = "") String searchValue) {
        UserSearchDto userSearchDto = UserSearchDto.builder()
                .searchKey(searchKey)
                .searchValue(searchValue)
                .build();
        return ResponseEntity.ok(userService.searchUsers(pageable, role, userSearchDto));
    }

    /**
     * 회원 삭제 처리
     *
     * @param userId 회원 고유 번호
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.ok().build();
    }
}
