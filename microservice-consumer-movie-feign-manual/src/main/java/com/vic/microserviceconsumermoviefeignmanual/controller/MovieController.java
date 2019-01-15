package com.vic.microserviceconsumermoviefeignmanual.controller;

import com.vic.microserviceconsumermoviefeignmanual.domain.entity.User;
import com.vic.microserviceconsumermoviefeignmanual.feign.UserFeignClient;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Import(FeignClientsConfiguration.class)
@RestController
public class MovieController {

    private UserFeignClient userUserFeignClient;

    private UserFeignClient adminUserFeignClient;

    @Autowired
    public MovieController(Decoder decoder, Encoder encoder, Client client, Contract contract){
        // decoder、encoder、client、contract 可以查看实例
        this.userUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("user","user123"))
                .target(UserFeignClient.class,"http://microservice-provider-user-with-auth");
        this.adminUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("admin","admin123"))
                .target(UserFeignClient.class,"http://microservice-provider-user-with-auth");

    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public User findByIdUser(@PathVariable Long id) {
        return this.userUserFeignClient.findById(id);
    }

    @GetMapping(value = "/user-admin/{id}", produces = "application/json")
    public User findByIdAdmin(@PathVariable Long id) {
        System.out.println(id);
        return this.adminUserFeignClient.findById(id);
    }

}
