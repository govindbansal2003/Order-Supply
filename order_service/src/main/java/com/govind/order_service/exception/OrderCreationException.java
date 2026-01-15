package com.govind.order_service.exception;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message,Throwable cause) {
        super(message,cause);
    }
}
