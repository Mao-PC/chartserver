package com.amo.chartserver.util.exception;

public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
