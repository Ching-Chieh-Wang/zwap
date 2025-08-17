package com.zwap.product_write_service.client;


import com.zwap.product_common.exception.ProductNotFoundException;
import com.zwap.product_read_service.grpc.IsProductOwnerGrpcRequest;
import com.zwap.product_read_service.grpc.IsProductOwnerGrpcResponse;
import com.zwap.product_read_service.grpc.ProductReadServiceGrpc.ProductReadServiceBlockingStub;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProductReadGrpcClient {

    @Resource
    private ProductReadServiceBlockingStub productReadStub;


    public boolean isProductOwner(String id, String userId) {
        try {
            IsProductOwnerGrpcRequest request = IsProductOwnerGrpcRequest.newBuilder()
                    .setId(id)
                    .setUserId(userId)
                    .build();

            IsProductOwnerGrpcResponse response = productReadStub.isProductOwner(request);
            return response.getIsProductOwner();
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new ProductNotFoundException(id);
            }
            throw new RuntimeException("Failed to get product by ID: " + id, e);
        }
    }
}