package com.veloMTL.veloMTL.DTO.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;

import java.time.LocalDateTime;

public class BikeDTO {

    private String bikId;
    private String bikeType;
    private String dockId;
    private BikeStatus bikeStatus;
    private LocalDateTime reserveDate;
    private String reserveUser;

    public BikeDTO(){};

    public BikeDTO(String bikId, String bikeType, String dockId, BikeStatus bikeStatus) {
        this.bikId = bikId;
        this.bikeType = bikeType;
        this.dockId = dockId;
        this.bikeStatus = bikeStatus;
    }


    public String getBikId() {
        return bikId;
    }

    public void setBikId(String bikId) {
        this.bikId = bikId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public String getDockId() {
        return dockId;
    }

    public void setDockId(String dockId) {
        this.dockId = dockId;
    }

    public BikeStatus getBikeStatus() {
        return bikeStatus;
    }

    public void setBikeStatus(BikeStatus bikeStatus) {
        this.bikeStatus = bikeStatus;
    }

    public LocalDateTime getReserveDate() { return reserveDate; }

    public String getReserveUser() { return reserveUser; }

    public void setReserveUser(String reserveUser) {
        this.reserveUser = reserveUser;
    }

    public void setReserveDate(LocalDateTime reserveDate) {
        this.reserveDate = reserveDate;
    }
}
