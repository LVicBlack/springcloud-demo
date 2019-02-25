package com.vic.microserviceconsumermovie.feign;

import com.vic.microserviceconsumermovie.config.FeignConfiguration;
import com.vic.microserviceconsumermovie.domain.entity.User;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "microservice-provider-user", configuration = FeignConfiguration.class)
public interface UserFeignClient {

//    非自定义Feign

    // 非自定义情况下 不支持使用@GetMapping
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public User findById(@PathVariable("id") Long id);

    // 该请求不会成功
//    @RequestMapping(value = "/get", method = RequestMethod.GET)
//    public User get0(User user);
//
//    @RequestMapping(value = "/get", method = RequestMethod.GET)
//    public User get1(@RequestParam("id") Long id, @RequestParam("username") String username);
//
//    @RequestMapping(value = "/get", method = RequestMethod.GET)
//    public User get2(@RequestParam Map<String, Object> map);
//
//    @RequestMapping(value = "/post", method = RequestMethod.POST)
//    public User post(@RequestBody User user);

//    -------------------------------
//    自定义Feign
    /**
     * 使用feign自带的注解@RequestLine
     *
     * @param id 用户id
     * @return 用户信息
     * @see https://github.com/OpenFeign/feign
     */
    @RequestLine("GET /{id}")
    public User findById(@Param("id") Long id);

    // 该请求不会成功
    @Headers("id: {id}, username: {username}")
    @RequestLine("GET /get")
    public User get0(User user);

    // 该请求没有传值过去
    @Headers("id: {id}, username: {username}")
    @RequestLine("GET /get")
    public User get1(@Param("id") Long id, @Param("username") String username);

    // feign自带注解暂时只有这一种方法成功
    @RequestLine("GET /get")
    public User get2(@QueryMap Map<String, Object> map);

    @RequestLine("POST /post")
    public User post(@RequestBody User user);
}