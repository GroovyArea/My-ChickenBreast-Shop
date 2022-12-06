package com.daniel.ddd.user.application.service;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.user.adaptor.out.persistence.UserRepository;
import com.daniel.ddd.user.application.dto.request.ModifyRequestDto;
import com.daniel.ddd.user.application.port.in.ManageUserUseCase;
import com.daniel.ddd.user.domain.enums.ErrorMessages;
import com.daniel.ddd.user.mapper.UserModifyMapper;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ManageUserService implements ManageUserUseCase {

    private final UserRepository userRepository;
    private final UserModifyMapper userModifyMapper;

    @Override
    public void modifyUser(Long userId, ModifyRequestDto modifyRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.USER_NOT_EXISTS.getMessage()));

        String updatedPassword;
        try {
            updatedPassword = PasswordEncrypt.getSecurePassword(modifyRequestDto.getPassword(), user.getSalt());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        User modifier = userModifyMapper.toEntity(modifyRequestDto);

        user.updateUserInfo(modifier, updatedPassword);
    }

    @Override
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.USER_NOT_EXISTS.getMessage()));

        user.remove();
    }
}
