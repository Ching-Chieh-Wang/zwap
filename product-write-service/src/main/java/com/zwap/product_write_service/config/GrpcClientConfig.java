// src/main/java/com/zwap/config/GrpcClientConfig.java
package com.zwap.product_write_service.config;

import com.zwap.product_read_service.grpc.ProductReadServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ProductReadServiceGrpc.ProductReadServiceBlockingStub productReadStub(GrpcChannelFactory channels) {
        return ProductReadServiceGrpc.newBlockingStub(channels.createChannel("product-read-service"));
    }
}