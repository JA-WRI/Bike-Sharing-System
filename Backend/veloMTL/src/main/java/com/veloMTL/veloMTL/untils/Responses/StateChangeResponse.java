package com.veloMTL.veloMTL.untils.Responses;

import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;

public class StateChangeResponse {

    private final StateChangeStatus status;
    private final String message;

    public StateChangeResponse(StateChangeStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StateChangeStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return status == StateChangeStatus.SUCCESS;
    }

}
