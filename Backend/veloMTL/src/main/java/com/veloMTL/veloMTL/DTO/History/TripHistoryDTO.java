package com.veloMTL.veloMTL.DTO.History;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;

public class TripHistoryDTO extends TripDTO {
    private final String bikeType;
    private final String originStation;
    private final String arrivalStation;
    private final double cost;

    public TripHistoryDTO(Trip trip) {
        super(trip.getTripId(), trip.getStartTime(), trip.getEndTime(), trip.getBike().getBikeId(),
                trip.getRider().getEmail());
        this.bikeType = trip.getBike().getBikeType();
        this.originStation = trip.getOriginStation();
        this.arrivalStation = trip.getArrivalStation();
        this.cost = (trip.getBilling() != null) ? trip.getBilling().getCost() : 0.0;
    }

    public String getBikeType() {
        return bikeType;
    }
    public String getOriginStation() { return originStation;}
    public String getArrivalStation() { return arrivalStation; }
    public double getCost() { return cost; }
}