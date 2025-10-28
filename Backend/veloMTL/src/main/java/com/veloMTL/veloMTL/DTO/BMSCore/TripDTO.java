package com.veloMTL.veloMTL.DTO.BMSCore;

import java.time.LocalDateTime;

public class TripDTO {
    private String tripId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bikeId;
    private String riderId;

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

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }
}
