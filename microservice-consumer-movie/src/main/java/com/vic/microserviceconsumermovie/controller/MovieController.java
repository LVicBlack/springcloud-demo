package com.vic.microserviceconsumermovie.controller;

import com.vic.microserviceconsumermovie.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://localhost:8011/" + id, User.class);
    }

    @GetMapping(value = "/user-instance", produces = "application/json")
    public List<ServiceInstance> showInfo() {
        return discoveryClient.getInstances("microservice-provider-user");
    }
}
