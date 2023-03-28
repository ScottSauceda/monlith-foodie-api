package com.foodie.monolith.exception;

public class NotCurrentUserException extends RuntimeException {
    private String message;
    public NotCurrentUserException() {}
    public NotCurrentUserException(String message) {
        super(message);
        this.message = message;
    }
}
