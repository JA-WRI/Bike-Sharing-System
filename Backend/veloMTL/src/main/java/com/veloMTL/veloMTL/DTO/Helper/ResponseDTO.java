package com.veloMTL.veloMTL.DTO.Helper;

import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;

import java.time.LocalDateTime;

public class ResponseDTO<T>{
    private StateChangeStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseDTO(StateChangeStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Convenience constructors
    public ResponseDTO(StateChangeStatus status, String message) {
        this(status, message, null);
    }

    public StateChangeStatus getStatus() {
        return status;
    }

    public void setStatus(StateChangeStatus status) {
        this.status = status;
    }

    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }


    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}
