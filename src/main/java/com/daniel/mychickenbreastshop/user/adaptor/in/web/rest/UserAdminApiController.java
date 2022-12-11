package com.daniel.mychickenbreastshop.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.user.application.port.in.ManageUserUseCase;
import com.daniel.mychickenbreastshop.user.application.port.in.UserSearchUseCase;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
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

    private final ManageUserUseCase userUseCase;
    private final UserSearchUseCase searchUseCase;

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
        return ResponseEntity.ok(searchUseCase.getAllUsers(pageable));
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
        return ResponseEntity.ok(searchUseCase.searchUsers(pageable, role, userSearchDto));
    }

    /**
     * 회원 삭제 처리
     *
     * @param userId 회원 고유 번호
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long userId) {
        userUseCase.removeUser(userId);
        return ResponseEntity.ok().build();
    }
}
