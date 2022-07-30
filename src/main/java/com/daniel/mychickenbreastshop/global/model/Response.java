package com.daniel.mychickenbreastshop.global.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private T data;
    private String message;

}

