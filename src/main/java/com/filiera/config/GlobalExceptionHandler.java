package com.filiera.config;

import com.filiera.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.filiera.model.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
    @Slf4j
    public class GlobalExceptionHandler {


        @ExceptionHandler(ProductNotFoundException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
            log.error("Product not found: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "PRODUCT_NOT_FOUND",
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }


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


        @ExceptionHandler(CuratorNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
            log.error("User not found: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(
                    "USER_NOT_FOUND",
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }


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

        @ExceptionHandler(EmptyCartException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponse> handleEmptyCartException(EmptyCartException ex) {

            log.warn("Operazione fallita per carrello vuoto: {}", ex.getMessage());

            ErrorResponse errorResponse = new ErrorResponse(
                    "EMPTY_CART",
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    @ExceptionHandler(EmptyOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleEmptyOrderException(EmptyOrderException ex) {

        log.warn("Tentativo di creare un ordine vuoto: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "EMPTY_ORDER",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InviteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleInviteNotFoundException(InviteNotFoundException ex) {

        log.warn("Tentativo di accedere a un invito non esistente: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "INVITE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException ex) {

        log.warn("Tentativo di accedere a un evento non esistente: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "EVENT_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}


