package com.mordiniaa.teamservice.exceptions;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException() {
        this("Team Not Found");
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
}
