package com.daniel.mychickenbreastshop.infra.application;

import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.EmailRequestDto;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final String ENCODE_TYPE = "utf-8";
    private static final long EXPIRED_TIME = 5 * 60 * 1000L; // 5분

    @Value("${email.from}")
    private String from;
    @Value("${email.subject}")
    private String subject;
    @Value("${email.text}")
    private String text;
    private Random random;

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Async("emailThreadPoolTaskExecutor")
    public void sendMail(EmailRequestDto emailRequestDto) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODE_TYPE);
            helper.setFrom(from);
            helper.setTo(emailRequestDto.getEmail());
            helper.setSubject(subject);

            int randomKey = random.nextInt(888888) + 111111;

            helper.setText(text + randomKey, true);
            javaMailSender.send(mimeMessage);

            redisService.setDataExpire(emailRequestDto.getEmail(), String.valueOf(randomKey), EXPIRED_TIME);
        } catch (MessagingException e) {
            throw new InternalErrorException(e);
        }
    }
}
