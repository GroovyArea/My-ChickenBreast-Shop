package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kakaopay")
public class KakaoPayClientProperty {

    private Api api;
    private Admin admin;
    private String url;
    private Uri uri;
    private Parameter parameter;
    private int readTime;
    private int connectTime;

    @Getter
    @Setter
    public static class Api {

        private String approval;
        private String cancel;
        private String fail;
    }

    @Getter
    @Setter
    public static class Admin {

        private String key;
    }

    @Getter
    @Setter
    public static class Uri {

        private String ready;
        private String approve;
        private String cancel;
        private String order;
    }

    @Getter
    @Setter
    public static class Parameter {

        private String cid;
        private int taxFree;
    }

}
