package com.vic.microserviceconfigclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Vic on 2019/3/12
 */
@RestController
public class TestController {
    @Value("${profile}")
    private String profile;



    @GetMapping("/profile")
    public String hello() {
        System.out.println(profile);
        return profile;
    }
}
