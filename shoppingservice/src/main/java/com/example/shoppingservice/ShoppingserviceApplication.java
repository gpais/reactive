package com.example.shoppingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
public class ShoppingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingserviceApplication.class, args);
	}
}
