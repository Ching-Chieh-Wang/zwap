package com.zwap.product_write_service.client;


import com.zwap.product_read_service.grpc.IsUserProductOwnerRequest;
import com.zwap.product_read_service.grpc.IsUserProductOwnerResponse;
import com.zwap.product_read_service.grpc.ProductReadServiceGrpc;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import io.grpc.Status;

@Service
public class ProductReadGrpcClient {

    @Resource
    private ProductReadServiceGrpc.ProductReadServiceBlockingStub productReadStub;

    public boolean isUserProductOwner(String userId, String productId) {
        try{
            IsUserProductOwnerRequest request  = IsUserProductOwnerRequest.newBuilder()
                    .setUserId(userId)
                    .setProductId(productId)
                    .build();
            IsUserProductOwnerResponse response = productReadStub.isUserProductOwner(request);
            return response.getIsOwner();
        }
        catch (Exception e) {
            throw Status.INTERNAL
                .withDescription("Failed to check product ownership: " + e.getMessage())
                .withCause(e)
                .asRuntimeException();
        }

    }
}