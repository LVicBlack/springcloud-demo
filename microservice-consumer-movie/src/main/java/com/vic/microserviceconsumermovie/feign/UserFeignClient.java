package com.vic.microserviceconsumermovie.feign;

import com.vic.microserviceconsumermovie.config.FeignConfiguration;
import com.vic.microserviceconsumermovie.domain.entity.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "microservice-provider-user",configuration = FeignConfiguration.class)
public interface UserFeignClient {
    // 非自定义情况下
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public User findById(@PathVariable("id") Long id);

    /**
     * 使用feign自带的注解@RequestLine
     * @see https://github.com/OpenFeign/feign
     * @param id 用户id
     * @return 用户信息
     */
    @RequestLine("GET /{id}")
    public User findById(@Param("id") Long id);
}