package com.multi.tent.pos_api.common.dto;

import java.time.Instant;

public class ErrorResponseDto {
    private String message;
    private Instant timestamp = Instant.now();
    private String path;


    public ErrorResponseDto(String message, String path) {
        this.message = message;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
