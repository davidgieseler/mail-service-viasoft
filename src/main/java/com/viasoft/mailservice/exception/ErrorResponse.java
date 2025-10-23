package com.viasoft.mailservice.exception;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {

    private final int status;
    private final String message;
    private final Instant timestamp;
    private final String path;

    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now();
    }
}
