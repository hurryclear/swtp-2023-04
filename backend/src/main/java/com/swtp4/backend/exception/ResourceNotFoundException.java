package com.swtp4.backend.exception;

// if an entity not has been found this Exception is thrown
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}