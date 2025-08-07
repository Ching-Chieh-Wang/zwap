package com.zwap.product_read_service.grpc;

import com.zwap.product_common.exception.ProductNotFoundException;
import com.zwap.product_read_service.service.IProductService;

import io.grpc.stub.StreamObserver;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import build.buf.protovalidate.Validator;
import build.buf.protovalidate.ValidatorFactory;
import build.buf.protovalidate.ValidationResult;

@Service
public class ProductReadGrpcService extends ProductReadServiceGrpc.ProductReadServiceImplBase {

    private static final Validator validator = ValidatorFactory.newBuilder().build();

    @Resource
    IProductService productService;

    @Override
    public void isUserProductOwner(IsUserProductOwnerRequest request,
                                   StreamObserver<IsUserProductOwnerResponse> responseObserver) {
        try {
            // ✅ Validate the request
            ValidationResult result = validator.validate(request);
            if (!result.isSuccess()) {
                responseObserver.onError(
                        io.grpc.Status.INVALID_ARGUMENT
                                .withDescription("Validation failed: " + result.getViolations().toString())
                                .asRuntimeException()
                );
                return;
            }

            String userId = request.getUserId();
            String productId = request.getProductId();

            boolean isOwner = productService.isUserOwner(userId, productId);

            IsUserProductOwnerResponse response = IsUserProductOwnerResponse.newBuilder()
                    .setIsOwner(isOwner)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ProductNotFoundException e) {
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Internal server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

}