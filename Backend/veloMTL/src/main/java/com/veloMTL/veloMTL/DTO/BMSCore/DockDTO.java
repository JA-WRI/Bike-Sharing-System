package com.veloMTL.veloMTL.DTO.BMSCore;


import com.veloMTL.veloMTL.Model.Enums.DockStatus;

import java.time.LocalDateTime;

public class DockDTO {
    private String dockId;
    private DockStatus status;
    private String stationId;
    private String bikeId;
    private LocalDateTime reserveDate;
    private String reserveUser;

    public DockDTO(){};

    public DockDTO(String dockId, DockStatus status, String stationId, String bikeId) {
        this.dockId = dockId;
        this.status = status;
        this.stationId = stationId;
        this.bikeId = bikeId;
    }

    public String getDockId() {
        return dockId;
    }

    public void setDockId(String dockId) {
        this.dockId = dockId;
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

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public LocalDateTime getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(LocalDateTime reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveUser() {
        return reserveUser;
    }

    public void setReserveUser(String reserveUser) {
        this.reserveUser = reserveUser;
    }
}
