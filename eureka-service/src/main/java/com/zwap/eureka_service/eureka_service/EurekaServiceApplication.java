package com.zwap.eureka_service.eureka_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServiceApplication.class, args);
	}

}
