package com.veloMTL.veloMTL.DTO;


public class DockDTO {
    private String id;
    private String status;
    private String stationId;

    public DockDTO(){};

    public DockDTO(String id, String status, String stationId) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
