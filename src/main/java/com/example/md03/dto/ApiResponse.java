package com.example.md03.dto;

public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private T data;

    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}