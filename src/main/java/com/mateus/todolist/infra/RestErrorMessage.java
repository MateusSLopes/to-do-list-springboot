package com.mateus.todolist.infra;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class RestErrorMessage {
    private final Instant moment;
    private final HttpStatus httpStatus;
    private final String message;

    public RestErrorMessage(Instant moment, HttpStatus httpStatus, String message) {
        this.moment = moment;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Instant getMoment() {
        return moment;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
