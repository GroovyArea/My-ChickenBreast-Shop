package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.domain.user.dto.UserJoinDTO;
import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessages;
import com.daniel.mychickenbreastshop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 가입 컨트롤러
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.03 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/join")
public class JoinController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> join(@RequestBody UserJoinDTO userJoinDTO) {
        userService.registerUser(userJoinDTO);
        return ResponseEntity.ok(ResponseMessages.JOIN_SUCCEED_MESSAGE.getMessage());
    }

}
