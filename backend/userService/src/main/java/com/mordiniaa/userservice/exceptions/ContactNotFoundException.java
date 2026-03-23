package com.mordiniaa.userservice.exceptions;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException() {
        this("Contact Not Found");
    }

    public ContactNotFoundException(String message) {
        super(message);
    }
}
