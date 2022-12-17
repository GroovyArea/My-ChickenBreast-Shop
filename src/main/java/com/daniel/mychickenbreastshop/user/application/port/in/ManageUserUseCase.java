package com.daniel.mychickenbreastshop.user.application.port.in;

import com.daniel.mychickenbreastshop.user.model.dto.request.ModifyRequestDto;

public interface ManageUserUseCase {

    void modifyUser(Long userId, ModifyRequestDto modifyRequestDto);

    void removeUser(Long userId);
}
