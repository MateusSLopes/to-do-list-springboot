package com.mateus.todolist.domain.enums;

public enum TaskStatus {
    PENDING(0),
    STOPPED(1),
    IN_PROGRESS(2),
    DONE(3);

    private int code;

    private TaskStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskStatus valueOf(int code) {
        for (TaskStatus t : TaskStatus.values()){
            if (t.code == code)
                return t;
        }
        throw new IllegalArgumentException("This task status code is invalid!");
    }
}
