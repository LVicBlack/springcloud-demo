package com.vic.microserviceconsumermovie.controller;

import com.vic.microserviceconsumermovie.domain.entity.User;
import com.vic.microserviceconsumermovie.feign.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private UserFeignClient userFeignClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://localhost:8011/" + id, User.class);
    }

    @GetMapping(value = "/feign/user/{id}", produces = "application/json")
    public User feignFindById(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }

    /**
     * 查询microservice-provider-user服务的信息并返回
     * @return microservice-provider-user服务的信息
     */
    @GetMapping(value = "/user-instance", produces = "application/json")
    public List<ServiceInstance> showInfo() {
        return discoveryClient.getInstances("microservice-provider-user");
    }

    // Ribbon
    @GetMapping(value = "/log-instance", produces = "application/json")
    public ServiceInstance logUserInstance() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-provider-user");
        LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(),serviceInstance.getHost(), serviceInstance.getPort());
        return serviceInstance;
    }
}
