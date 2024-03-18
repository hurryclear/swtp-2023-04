package com.swtp4.backend.exception;

// Error Response for Frontend with just a message
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
