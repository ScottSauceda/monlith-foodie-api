package com.foodie.monolith.exception;

public class EmailTakenException extends RuntimeException {
    private String message;

    public EmailTakenException() {}

    public EmailTakenException(String message) {
        super(message);
        this.message = message;
    }
}