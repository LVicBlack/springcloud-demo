package com.vic.microserviceprovideruserwithauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceProviderUserWithAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProviderUserWithAuthApplication.class, args);
	}

}

