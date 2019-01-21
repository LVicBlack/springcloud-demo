package com.vic.microserviceconsumermoviefeignmanual.controller;

import com.vic.microserviceconsumermoviefeignmanual.domain.entity.User;
import com.vic.microserviceconsumermoviefeignmanual.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {


    @Autowired
    private UserFeignClient userFeignClient;

    // 使用Feign进行请求
    @GetMapping(value = "/feign/user/{id}", produces = "application/json")
    public User feignFindById(@PathVariable(name = "id") Long id) {
        System.out.println(id);
        return userFeignClient.findById(id);
    }

}
