package com.daniel.mychickenbreastshop.user.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.application.port.in.ManageUserUseCase;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.user.mapper.UserModifyMapper;
import com.daniel.mychickenbreastshop.user.model.dto.request.ModifyRequestDto;
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
