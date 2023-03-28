package com.foodie.monolith.exception;

public class ImageTypeException extends RuntimeException {
    private String message;

    public ImageTypeException() {}

    public ImageTypeException(String message) {
        super(message);
        this.message = message;
    }
}
