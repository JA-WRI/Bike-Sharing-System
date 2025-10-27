package com.veloMTL.veloMTL.DTO.BMSCore;

public class TripDTO {

    private String tripId;
    private String startTime;
    private String endTime;
    private String bikeId;
    private String riderId;

    public TripDTO(){};

    public TripDTO(String tripId, String startTime, String endTime, String bikeId, String riderId) {
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

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
//    public void setBikId(String bikId) {
//        this.bikId = bikId;
//    }
//
//    public String getBikeType() {
//        return bikeType;
//    }
//
//    public void setBikeType(String bikeType) {
//        this.bikeType = bikeType;
//    }
//
//    public String getDockId() {
//        return dockId;
//    }
//
//    public void setDockId(String dockId) {
//        this.dockId = dockId;
//    }
//
//    public BikeStatus getBikeStatus() {
//        return bikeStatus;
//    }
//
//    public void setBikeStatus(BikeStatus bikeStatus) {
//        this.bikeStatus = bikeStatus;
//    }
}
