package com.filiera.config;

import com.filiera.exception.*;
import com.filiera.model.exception.*;
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
/*
    // Handle validation errors for @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            // Extract just the parameter name (remove method name prefix)
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());
        });

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_FAILED",
                "Validation failed for request parameters",
                HttpStatus.BAD_REQUEST.value(),
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle validation errors for @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Method argument validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_FAILED",
                "Validation failed for request body",
                HttpStatus.BAD_REQUEST.value(),
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle HTTP message not readable (malformed JSON)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON request: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "MALFORMED_JSON",
                "Request body contains invalid JSON",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle missing request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("Missing request parameter: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "MISSING_PARAMETER",
                "Required parameter '" + ex.getParameterName() + "' is missing",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle method not allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("Method not allowed: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                "METHOD_NOT_ALLOWED",
                "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint",
                HttpStatus.METHOD_NOT_ALLOWED.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handle generic RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_ERROR",
                "An internal error occurred while processing your request",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    */
    }


