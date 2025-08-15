package com.zwap.product_common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED)
public class ProductPendingException extends RuntimeException {
    public ProductPendingException(String message) {
        super(message);
    }
}
