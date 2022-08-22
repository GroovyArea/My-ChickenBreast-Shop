package com.daniel.mychickenbreastshop.infra.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MailDto {

    private String title;
    private String to;
    private String from;
    private String content;
}
