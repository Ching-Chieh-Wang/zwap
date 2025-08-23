package com.zwap.product.product_database.product_read_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "com.zwap.product.product_database.product_database_common.mapper")
public class ProductReadServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductReadServiceApplication.class, args);
	}
}
