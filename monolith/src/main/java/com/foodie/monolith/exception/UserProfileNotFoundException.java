package com.foodie.monolith.exception;

public class UserProfileNotFoundException extends RuntimeException {
    private String message;

    public UserProfileNotFoundException() {}

    public UserProfileNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}