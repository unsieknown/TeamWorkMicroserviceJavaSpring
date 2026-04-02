package com.mordiniaa.bordservice.exceptions;

public class TaskCategoryNotFoundException extends RuntimeException {
    public TaskCategoryNotFoundException(String message) {
        super(message);
    }
}
