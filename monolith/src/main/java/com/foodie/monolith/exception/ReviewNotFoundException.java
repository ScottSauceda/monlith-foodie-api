package com.foodie.monolith.exception;

public class ReviewNotFoundException extends Exception {
    private String message;

    public ReviewNotFoundException() {}

    public ReviewNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
