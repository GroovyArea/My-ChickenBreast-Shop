package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.DetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;

    /**
     * 회원 디테일 조회
     *
     * @return 회원 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<DetailResponseDto> getUserDetail(@PathVariable Long userId) {
        DetailResponseDto userDTO = userService.getUser(userId);
        return ResponseEntity.ok().body(userDTO);
    }

    /**
     * 개인 회원 정보 수정
     *
     * @param modifyDTO 회원 수정 정보
     */
    @PatchMapping
    public ResponseEntity<Void> modifyUser(@Valid @RequestBody ModifyRequestDto modifyDTO) {
        Long userId = getUserId();
        userService.modifyUser(userId, modifyDTO);
        return ResponseEntity.ok().build();
    }

    private Long getUserId() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalDetails.getId();
    }
}
