package com.zwap.product_write_service.client;


import com.zwap.product_common.entity.Product;
import com.zwap.product_read_service.grpc.ProductId;
import com.zwap.product_read_service.grpc.ProductReadServiceGrpc.ProductReadServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProductReadGrpcClient {

    @Resource
    private ProductReadServiceBlockingStub productReadStub;


    public Product getProductById(String id) {
        try {
            ProductId request = ProductId.newBuilder()
                    .setId(id)
                    .build();

            return new Product(productReadStub.getProductById(request));
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Failed to get product by ID: " + id, e);
        }
    }
}