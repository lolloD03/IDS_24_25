package com.filiera.config;

import com.filiera.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.filiera.model.dto.ErrorResponse;


    @ControllerAdvice
    @Slf4j
    public class GlobalExceptionHandler {

        // Handle custom ProductNotFoundException
        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
            log.error("Product not found: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "PRODUCT_NOT_FOUND",
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Handle custom InsufficientQuantityException
        @ExceptionHandler(InsufficientQuantityException.class)
        public ResponseEntity<ErrorResponse> handleInsufficientQuantityException(InsufficientQuantityException ex) {
            log.error("Insufficient quantity: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "INSUFFICIENT_QUANTITY",
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Handle IllegalArgumentException
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
            log.error("Invalid argument: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "INVALID_ARGUMENT",
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }


        // Handle custom ProductNotPendingException
        @ExceptionHandler(ProductNotPendingException.class)
        public ResponseEntity<ErrorResponse> handleProductNotPendingException(ProductNotPendingException ex) {
            log.error("Product not pending approval: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "PRODUCT_NOT_PENDING",
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Handle custom CuratorNotFoundException
        @ExceptionHandler(CuratorNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCuratorNotFoundException(CuratorNotFoundException ex) {
            log.error("Curator not found: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "CURATOR_NOT_FOUND",
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Handle custom InvalidUserTypeException
    @ExceptionHandler(InvalidUserTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserTypeException(InvalidUserTypeException ex) {
        log.error("Invalid user type: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_USER_TYPE",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

        @ExceptionHandler(UnauthorizedOperationException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorizedOperationException(UnauthorizedOperationException ex) {
            log.error("Unauthorized operation: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "UNAUTHORIZED_OPERATION",
                    ex.getMessage(),
                    HttpStatus.FORBIDDEN.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

    }


