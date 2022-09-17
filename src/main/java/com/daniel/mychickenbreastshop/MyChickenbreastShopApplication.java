package com.daniel.mychickenbreastshop;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.config.KakaoPayClientProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KakaoPayClientProperty.class)
public class MyChickenbreastShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyChickenbreastShopApplication.class, args);
    }

}
