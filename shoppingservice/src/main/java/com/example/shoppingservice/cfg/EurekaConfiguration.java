package com.example.shoppingservice.cfg;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEurekaClient
@EnableDiscoveryClient
public class EurekaConfiguration {

}
