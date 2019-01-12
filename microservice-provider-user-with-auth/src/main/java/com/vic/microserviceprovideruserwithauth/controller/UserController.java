package com.vic.microserviceprovideruser.controller;

import com.vic.microserviceprovideruser.dao.UserRepository;
import com.vic.microserviceprovideruser.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
