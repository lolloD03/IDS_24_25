package com.filiera.model.exception;

public class CuratorNotFoundException extends RuntimeException {
    public CuratorNotFoundException(String message) {
        super(message);
    }
}