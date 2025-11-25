package com.veloMTL.veloMTL.DTO.History;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;

import java.time.LocalDateTime;

public class TripHistoryDTO extends TripDTO {
    private final String bikeType;
    private final String originStation;
    private final String arrivalStation;
    private final double cost;
    private final LocalDateTime reserveTime;
    private final boolean reservationExpired;

    public TripHistoryDTO(Trip trip) {
        super(trip.getTripId(), trip.getStartTime(), trip.getEndTime(), trip.getBike().getBikeId(),
                trip.getUserEmail());
        this.bikeType = trip.getBike().getBikeType();
        this.originStation = trip.getOriginStation();
        this.arrivalStation = trip.getArrivalStation();
        this.cost = (trip.getBilling() != null) ? trip.getBilling().getCost() : 0.0;
        this.reserveTime = trip.getBike().getReserveDate() != null ? trip.getBike().getReserveDate() : null;
        this.reservationExpired = trip.isReservationExpired();
    }

    public String getBikeType() {
        return bikeType;
    }
    public String getOriginStation() { return originStation;}
    public String getArrivalStation() { return arrivalStation; }
    public double getCost() { return cost; }
    public LocalDateTime getReserveTime() { return reserveTime; }
    public boolean isReservationExpired() { return reservationExpired; }
}