package com.devamateur.administrator.identity_service.exception;

public enum ErrorCode {
    USER_ALREADY_EXISTS(1001, "User already exists"),
    USER_NOT_FOUND(1002, "User not found"),
    USER_INVALID(1003, "Username must be at least 5 characters long"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters long"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    UNAUTHENTICATED(1005, "Invalid username or password");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
