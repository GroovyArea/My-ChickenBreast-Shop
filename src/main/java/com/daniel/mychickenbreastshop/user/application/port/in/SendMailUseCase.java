package com.daniel.mychickenbreastshop.user.application.port.in;

import com.daniel.mychickenbreastshop.user.model.dto.request.EmailRequestDto;

public interface SendMailUseCase {

    void sendMail(EmailRequestDto emailRequestDto);
}
