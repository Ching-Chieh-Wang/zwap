package com.zwap.product_write_service.product_write_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.zwap.common.common.entity")
@EnableJpaRepositories(basePackages = "com.zwap.product_write_service.product_write_service.mapper")
public class ProductWriteServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductWriteServiceApplication.class, args);
	}
}
