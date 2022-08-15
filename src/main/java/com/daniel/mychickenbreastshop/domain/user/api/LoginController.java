package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.auth.jwt.JwtTokenProvider;
import com.daniel.mychickenbreastshop.domain.user.dto.UserLoginDTO;
import com.daniel.mychickenbreastshop.domain.user.dto.response.UserLoginResponseDto;
import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessages;
import com.daniel.mychickenbreastshop.domain.user.service.UserService;
import com.daniel.mychickenbreastshop.global.model.Response;
import com.daniel.mychickenbreastshop.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 컨트롤러
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.14 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @PostMapping
    public Response<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginResponseDto userLoginResponseDto = userService.login(userLoginDTO);
        String accessToken = jwtTokenProvider.createToken(String.valueOf(userLoginResponseDto.getId()), userLoginResponseDto.getRoles());
        redisService.setData(String.valueOf(userLoginResponseDto.getId()), accessToken);
        return Response.<String>builder()
                .data(accessToken)
                .message(ResponseMessages.LOGIN_SUCCEED_MESSAGE.getMessage())
                .build();
    }
}
