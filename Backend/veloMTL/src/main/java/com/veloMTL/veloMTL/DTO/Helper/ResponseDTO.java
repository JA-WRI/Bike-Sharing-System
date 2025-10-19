package com.veloMTL.veloMTL.DTO.Helper;

import java.time.LocalDateTime;

public class ResponseDTO<T>{
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Convenience constructors
    public ResponseDTO(boolean success, String message) {
        this(success, message, null);
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}
