package com.mordiniaa.taskservice.exceptions;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        this("Task Not Found");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
