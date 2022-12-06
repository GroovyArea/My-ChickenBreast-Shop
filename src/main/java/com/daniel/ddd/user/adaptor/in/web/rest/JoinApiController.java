package com.daniel.ddd.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.domain.user.application.MailService;
import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.EmailRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.JoinRequestDto;
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
 *     1.1 2022.12.06 패키지 구조 변경
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/join")
public class JoinApiController {

    private final UserService userService;
    private final MailService mailService;

    @PostMapping
    public ResponseEntity<Long> join(@RequestBody JoinRequestDto joinRequestDto) {
        return ResponseEntity.ok(userService.join(joinRequestDto));
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmailKey(@RequestBody EmailRequestDto emailRequestDto) {
        mailService.sendMail(emailRequestDto);
        return ResponseEntity.ok().build();
    }

}
