package com.mordiniaa.taskservice.exceptions;

public class TaskCategoryNotFoundException extends RuntimeException {
    public TaskCategoryNotFoundException(String message) {
        super(message);
    }
}
