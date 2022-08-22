package com.daniel.mychickenbreastshop.domain.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class asdf {

    @GetMapping("/api/v1/get")
    public String a() {
        return "aa";
    }
}
