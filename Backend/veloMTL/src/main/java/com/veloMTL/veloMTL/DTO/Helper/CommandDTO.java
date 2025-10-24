package com.veloMTL.veloMTL.DTO.Helper;

import java.time.LocalDateTime;

public class CommandDTO {
    private String command;
    private String userId;
    private String objectId;
    private String dockId;
    private LocalDateTime reserveTime;

    public CommandDTO(String command, String userId, String objectId, String dockId) {
        this.command = command;
        this.userId = userId;
        this.objectId = objectId;
        this.dockId = dockId;
        this.reserveTime = LocalDateTime.now();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDockId() {
        return dockId;
    }

    public void setDockId(String dockId) {
        this.dockId = dockId;
    }

    public LocalDateTime getReserveTime() { return this.reserveTime; }
}
