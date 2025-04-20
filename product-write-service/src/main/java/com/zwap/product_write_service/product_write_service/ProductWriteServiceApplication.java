package com.zwap.product_write_service.product_write_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.zwap.product_write_service.product_write_service.mapper")
public class ProductWriteServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductWriteServiceApplication.class, args);
	}
}
