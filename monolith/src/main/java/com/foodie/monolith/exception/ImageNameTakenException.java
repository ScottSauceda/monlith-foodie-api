package com.foodie.monolith.exception;

public class ImageNameTakenException extends RuntimeException {
    private String message;
    public ImageNameTakenException() {}
    public ImageNameTakenException(String message) {
        super(message);
        this.message = message;
    }
}