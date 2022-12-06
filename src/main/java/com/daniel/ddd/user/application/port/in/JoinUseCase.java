package com.daniel.ddd.user.application.port.in;

import com.daniel.ddd.user.application.dto.request.JoinRequestDto;

public interface JoinUseCase {

    Long join(JoinRequestDto joinRequestDto);


}
