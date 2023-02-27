package com.foodie.monolith.exception;

public class RoleNotFoundException extends Exception {
    private String message;

    public RoleNotFoundException() {}

    public RoleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
