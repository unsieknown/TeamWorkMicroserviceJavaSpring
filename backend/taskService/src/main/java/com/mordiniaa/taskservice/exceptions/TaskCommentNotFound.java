package com.mordiniaa.taskservice.exceptions;

public class TaskCommentNotFound extends RuntimeException {

    public TaskCommentNotFound() {
        super("Task Comment Not Found");
    }

    public TaskCommentNotFound(String message) {
        super(message);
    }
}
