package com.veloMTL.veloMTL.DTO.BMSCore;

import java.time.LocalDateTime;

public class TripDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bikeId;
    private String riderId;
    private String tripId;

    public TripDTO(){};

    public TripDTO(String tripId, LocalDateTime startTime, LocalDateTime endTime, String bikeId, String riderId) {
        this.tripId = tripId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bikeId = bikeId;
        this.riderId = riderId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getRiderId() {
        return riderId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }
}
