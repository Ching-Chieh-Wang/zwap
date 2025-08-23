package com.zwap.product.product_database.product_database_common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProductOwnershipException extends RuntimeException {
    public ProductOwnershipException(String message) {
        super(message);
    }
}
