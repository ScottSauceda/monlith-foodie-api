package com.foodie.monolith.exception;

public class UsernameTakenException extends RuntimeException {
    private String message;

    public UsernameTakenException() {}

    public UsernameTakenException(String message) {
        super(message);
        this.message = message;
    }
}