package com.vic.microserviceconsumermoviefeignhystrix.feign;


import com.vic.microserviceconsumermoviefeignhystrix.config.FeignDisableHystrixConfiguration;
import com.vic.microserviceconsumermoviefeignhystrix.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Feign禁用Hystrix的禁用配置类
//@FeignClient(name="microservice-provider-user", configuration = FeignDisableHystrixConfiguration.class)
public interface FeignDisableHystrixClient {
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable("id") Long id);
}
