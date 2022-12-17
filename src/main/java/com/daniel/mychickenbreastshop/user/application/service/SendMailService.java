package com.daniel.mychickenbreastshop.user.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.user.application.port.in.SendMailUseCase;
import com.daniel.mychickenbreastshop.user.application.service.redis.model.EmailRedisModel;
import com.daniel.mychickenbreastshop.user.model.dto.request.EmailRequestDto;
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
public class SendMailService implements SendMailUseCase {

    private static final String ENCODE_TYPE = "utf-8";
    private static final long EXPIRED_TIME = 5 * 60 * 1000L; // 5분

    @Value("${spring.mail.from}")
    private String from;
    @Value("${spring.mail.subject}")
    private String subject;
    @Value("${spring.mail.text}")
    private String text;

    private final JavaMailSender javaMailSender;
    private final RedisStore userRedisStore;

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

            EmailRedisModel redisEntity = createEntity(emailRequestDto, randomKey);

            userRedisStore.setDataExpire(redisEntity.getEmail(), redisEntity, EXPIRED_TIME);
        } catch (MessagingException | NoSuchAlgorithmException e) {
            throw new InternalErrorException(e);
        }
    }

    private EmailRedisModel createEntity(EmailRequestDto emailRequestDto, int randomKey) {
        return new EmailRedisModel(emailRequestDto.getEmail(), String.valueOf(randomKey));
    }
}
