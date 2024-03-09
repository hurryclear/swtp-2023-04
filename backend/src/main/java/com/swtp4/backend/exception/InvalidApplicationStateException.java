package com.swtp4.backend.exception;

public class InvalidApplicationStateException extends RuntimeException {
    public InvalidApplicationStateException(String message) {
        super(message);
    }
}