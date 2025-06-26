package com.zwap.product_read_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductReadServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductReadServiceApplication.class, args);
	}
}
