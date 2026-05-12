package com.multi.tent.pos_api.common.dto;

import java.time.Instant;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response body")
public class ErrorResponseDto {
    @Schema(description = "Error message", example = "Resource not found")
    private String message;

    @Schema(description = "Timestamp when the error occurred")
    private Instant timestamp = Instant.now();

    @Schema(description = "Request path that caused the error", example = "/api/v1/categories/123")
    private String path;

    @Schema(description = "Field-level validation errors (only for validation failures)")
    private Map<String, String> errors;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public ErrorResponseDto(String message, String path) {
        this.message = message;
        this.path = path;
    }

    public ErrorResponseDto(String message, String path, Map<String, String> errors) {
        this.message = message;
        this.path = path;
        this.errors = errors;
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

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
