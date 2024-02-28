package com.swtp4.backend.exception;

public class CustomErrorResponse {
    private String errorMessage;

    public CustomErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getters and setters
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
