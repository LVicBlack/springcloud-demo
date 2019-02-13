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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 使用restTemplate进行请求
    @GetMapping(value = "/user/{id}", produces = "application/json")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://microservice-provider-user/" + id, User.class);
    }
    // 使用restTemplate进行请求
    @GetMapping(value = "/test/user/{id}", produces = "application/json")
    public User findByIds(@PathVariable Long id) {
        return restTemplate.getForObject("http://microservice-provider-user/{id}", User.class, id);
    }

    // 使用Feign进行请求
    @GetMapping(value = "/feign/user/{id}", produces = "application/json")
    public User feignFindById(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }

    /**
     * 测试URL：http://localhost:8021/user/get0?id=1&username=张三
     * 该请求不会成功。
     * @param user user
     * @return 用户信息
     */
    @GetMapping(value = "/user/get0", produces = "application/json")
    public User get0(User user) {
        return this.userFeignClient.get0(user);
    }

    /**
     * 测试URL：http://localhost:8021/user/get1?id=1&username=张三
     * @param user user
     * @return 用户信息
     */
    @GetMapping(value = "/user/get1", produces = "application/json")
    public User get1(User user) {
        return this.userFeignClient.get1(user.getId(), user.getUsername());
    }

    /**
     * 测试URL：http://localhost:8021/user/get2?id=1&username=张三
     * @param user user
     * @return 用户信息
     */
    @GetMapping(value = "/user/get2", produces = "application/json")
    public User get2(User user) {
        Map<String, Object> map = new HashMap<>(8);
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        return this.userFeignClient.get2(map);
    }

    /**
     * 测试URL:http://localhost:8021/user/post?id=1&username=张三
     * @param user user
     * @return 用户信息
     */
    @PostMapping(value = "/user/post", produces = "application/json")
    public User post(User user) {
        return this.userFeignClient.post(user);
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
