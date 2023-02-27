package com.foodie.monolith.exception;

public class LocationNotFoundException extends RuntimeException {
    private String message;

    public LocationNotFoundException() {}

    public LocationNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}