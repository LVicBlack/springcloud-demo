package com.vic.microserviceconsumermoviefeignmanual.feign;

import com.vic.microserviceconsumermoviefeignmanual.config.FeignConfiguration;
import com.vic.microserviceconsumermoviefeignmanual.domain.entity.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "microservice-provider-user-with-auth", configuration = FeignConfiguration.class)
public interface UserFeignClient {
    /**
     * 使用feign自带的注解@RequestLine
     *
     * @param id 用户id
     * @return 用户信息
     * @see https://github.com/OpenFeign/feign
     */

    @RequestLine("GET /{id}")
    public User findById(@Param("id") Long id);
}