package com.veloMTL.veloMTL.DTO.Helper;

public class CommandDTO {
    private String command;
    private String userId;
    private String objectId;

    public CommandDTO(String command, String userId, String objectId) {
        this.command = command;
        this.userId = userId;
        this.objectId = objectId;
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
}
