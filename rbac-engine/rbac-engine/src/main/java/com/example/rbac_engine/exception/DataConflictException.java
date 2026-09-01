package com.example.rbac_engine.exception;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String message) {
        super(message);
    }
}
