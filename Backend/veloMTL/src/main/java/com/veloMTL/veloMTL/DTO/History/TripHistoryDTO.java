package com.veloMTL.veloMTL.DTO.History;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;

public class TripHistoryDTO extends TripDTO { //This class is nested as it is only for History
    private final String bikeType;

    public TripHistoryDTO(Trip trip) {
        super(trip.getTripId(), trip.getStartTime(), trip.getEndTime(), trip.getBike().getBikeId(),
                trip.getRider().getEmail());
        this.bikeType = trip.getBike().getBikeType();
    }

    public String getBikeType() {
        return bikeType;
    }
}