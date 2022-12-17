package com.daniel.mychickenbreastshop.user.application.port.in;

import com.daniel.mychickenbreastshop.user.model.dto.request.JoinRequestDto;

public interface JoinUseCase {

    Long join(JoinRequestDto joinRequestDto);


}
