package com.ninespokes.challenge.exception;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
