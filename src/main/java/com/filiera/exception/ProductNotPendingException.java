package com.filiera.exception;

public class ProductNotPendingException extends RuntimeException {
    public ProductNotPendingException(String message) {
        super(message);
    }
}