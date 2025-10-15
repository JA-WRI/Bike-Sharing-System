package com.veloMTL.veloMTL.DTO;


import com.veloMTL.veloMTL.Model.Enums.DockStatus;

public class DockDTO {
    private String id;
    private DockStatus status;
    private String stationId;

    public DockDTO(){};

    public DockDTO(String id, DockStatus status, String stationId) {
        this.id = id;
        this.status = status;
        this.stationId = stationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DockStatus getStatus() {
        return status;
    }

    public void setStatus(DockStatus status) {
        this.status = status;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
