package com.zwap.product.product_database.product_read_service.grpc;

import build.buf.protovalidate.ValidationResult;
import com.zwap.product.product_database.product_database_common.exception.ProductNotFoundException;
import com.zwap.product.product_database.product_database_common.grpc.IsProductOwnerGrpcRequest;
import com.zwap.product.product_database.product_database_common.grpc.IsProductOwnerGrpcResponse;
import com.zwap.product.product_database.product_database_common.grpc.ProductReadServiceGrpc;
import com.zwap.product.product_database.product_read_service.service.IProductService;

import io.grpc.stub.StreamObserver;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import build.buf.protovalidate.Validator;
import build.buf.protovalidate.ValidatorFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductReadGrpcService extends ProductReadServiceGrpc.ProductReadServiceImplBase {

    private static final Validator validator = ValidatorFactory.newBuilder().build();

    @Resource
    IProductService productService;

    @Override
    public void isProductOwner(IsProductOwnerGrpcRequest request,
                               StreamObserver<IsProductOwnerGrpcResponse> responseObserver){
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

            String id = request.getId();
            String userId = request.getUserId();


            boolean isOwner = productService.isProductOwnder(id, userId);
            IsProductOwnerGrpcResponse response = IsProductOwnerGrpcResponse.newBuilder()
                    .setIsProductOwner(isOwner)
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
            log.error("Error in isUserProductOwner: {}", e.getMessage(), e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription(e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

}