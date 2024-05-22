package com.mateus.todolist.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task not found!");
    }

    public TaskNotFoundException(String msg) {
        super(msg);
    }
}
