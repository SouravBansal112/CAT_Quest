package com.ren.catquest.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ErrorResponse {
    private final String message;
    private final int status;
    private final String error;
    private final Instant timestamp;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.timestamp = Instant.now();
    }
}
