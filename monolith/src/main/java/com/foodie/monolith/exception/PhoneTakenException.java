package com.foodie.monolith.exception;

public class PhoneTakenException extends RuntimeException {
    private String message;

    public PhoneTakenException() {}

    public PhoneTakenException(String message) {
        super(message);
        this.message = message;
    }
}