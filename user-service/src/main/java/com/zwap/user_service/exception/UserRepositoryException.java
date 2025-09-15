package com.zwap.user_service.exception;

public class UserRepositoryException extends RuntimeException {
    public UserRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}