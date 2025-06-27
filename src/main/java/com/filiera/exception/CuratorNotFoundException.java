package com.filiera.exception;

public class CuratorNotFoundException extends RuntimeException {
    public CuratorNotFoundException(String message) {
        super(message);
    }
}