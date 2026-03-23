package com.mordiniaa.authservice.exceptions;

public class SessionIntegrityException extends RuntimeException {
    public SessionIntegrityException(String message) {
        super(message);
    }
}
