package com.vic.microserviceconsumermoviefeignmanual.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FooConfiguration {
    /**
     * 对于服务端有security的情况，添加权限验证
     * @return 默认的feign契约
     */
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
        return new BasicAuthRequestInterceptor("user","user123");
    }
}
