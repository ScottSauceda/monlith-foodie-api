package com.foodie.monolith.exception;

public class ImageNotFoundException extends RuntimeException {

    private String message;
    public ImageNotFoundException() {}

    public ImageNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
