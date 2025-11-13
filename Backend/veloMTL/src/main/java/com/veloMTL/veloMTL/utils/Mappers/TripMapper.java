package com.veloMTL.veloMTL.utils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.*;
import com.veloMTL.veloMTL.Model.Users.Rider;

public class TripMapper {

    public static Trip dtoToEntity(TripDTO tripDTO, Bike bike, Rider rider) {
        Trip trip = new Trip();
        trip.setTripId(tripDTO.getTripId());
        trip.setBike(bike);
        trip.setRider(rider);
        trip.setStartTime(tripDTO.getStartTime());

        if(tripDTO.getEndTime() != null){
            trip.setEndTime(tripDTO.getEndTime());
        }

        return trip;
    }


    public static TripDTO entityToDto(Trip trip) {
        TripDTO tripdto = new TripDTO();
        tripdto.setTripId(trip.getTripId());
        // Use getUserId() which handles both Rider and Operator
        tripdto.setRiderId(trip.getUserId());
        tripdto.setBikeId(trip.getBike().getBikeId());
        tripdto.setStartTime(trip.getStartTime());

        if(trip.getEndTime() != null){
            tripdto.setEndTime(trip.getEndTime());
        }
        return tripdto;
    }

}
