package com.daniel.mychickenbreastshop.infra.application;

import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.EmailRequestDto;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final String ENCODE_TYPE = "utf-8";
    private static final long EXPIRED_TIME = 5 * 60 * 1000L; // 5분

    @Value("${spring.mail.from}")
    private String from;
    @Value("${spring.mail.subject}")
    private String subject;
    @Value("${spring.mail.text}")
    private String text;

    private final JavaMailSender javaMailSender;
    private final RedisStore redisStore;

    @Async("emailThreadPoolTaskExecutor")
    public void sendMail(EmailRequestDto emailRequestDto) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODE_TYPE);
            helper.setFrom(from);
            helper.setTo(emailRequestDto.getEmail());
            helper.setSubject(subject);

            int randomKey = SecureRandom.getInstanceStrong().nextInt(888888) + 111111;

            helper.setText(text + randomKey, true);
            javaMailSender.send(mimeMessage);

            redisStore.setDataExpire(emailRequestDto.getEmail(), String.valueOf(randomKey), EXPIRED_TIME);
        } catch (MessagingException | NoSuchAlgorithmException e) {
            throw new InternalErrorException(e);
        }
    }
}
