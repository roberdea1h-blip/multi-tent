package com.multi.tent.pos_api.common.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String field, String value) {
        super(field + " '" + value + "' already exists");
    }
}