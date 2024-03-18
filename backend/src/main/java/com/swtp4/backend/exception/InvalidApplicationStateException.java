package com.swtp4.backend.exception;

// if an application status is not possible backend throws this exception
public class InvalidApplicationStateException extends RuntimeException {
    public InvalidApplicationStateException(String message) {
        super(message);
    }
}