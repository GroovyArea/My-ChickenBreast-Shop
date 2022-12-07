package com.daniel.ddd.user.application.port.in;

import com.daniel.ddd.user.model.dto.request.ModifyRequestDto;

public interface ManageUserUseCase {

    void modifyUser(Long userId, ModifyRequestDto modifyRequestDto);

    void removeUser(Long userId);
}
