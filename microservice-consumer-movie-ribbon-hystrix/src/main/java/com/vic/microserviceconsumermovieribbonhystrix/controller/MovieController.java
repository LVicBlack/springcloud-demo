package com.vic.microserviceconsumermovieribbonhystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.vic.microserviceconsumermovieribbonhystrix.domain.entity.User;
import com.vic.microserviceconsumermovieribbonhystrix.feign.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private UserFeignClient userFeignClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @HystrixCommand(fallbackMethod = "findByIdFallback")
    @GetMapping(value = "/user/{id}", produces = "application/json")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://microservice-provider-user/" + id, User.class);
    }

    // 使用Feign进行请求
    @GetMapping(value = "/feign/user/{id}", produces = "application/json")
    public User feignFindById(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }

    // 使用hystrix进行请求
    @HystrixCommand(fallbackMethod = "findByIdFallback")
    @GetMapping(value = "/hystrix/user/{id}", produces = "application/json")
    public User hystrixFindById(@PathVariable Long id) {
        // 这里用到了RestTemplate的占位符能力
        User user = restTemplate.getForObject(
                "http://microservice-provider-user/{id}",
                User.class,
                id
        );
        // ...电影微服务的业务...
        return user;
    }

    public User findByIdFallback(Long id, Throwable throwable) {
        // 打印错误信息
//        LOGGER.error("进入回退方法", throwable);
        return new User(id, "默认用户", "默认用户", 0, new BigDecimal(1));
    }

}
