package com.veloMTL.veloMTL.utils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.*;

public class TripMapper {

    public static Trip dtoToEntity(TripDTO tripDTO, Bike bike, String userEmail) {
        Trip trip = new Trip();
        trip.setTripId(tripDTO.getTripId());
        trip.setBike(bike);
        trip.setUserEmail(userEmail);
        trip.setStartTime(tripDTO.getStartTime());

        if(tripDTO.getEndTime() != null){
            trip.setEndTime(tripDTO.getEndTime());
        }

        return trip;
    }


    public static TripDTO entityToDto(Trip trip) {
        TripDTO tripdto = new TripDTO();
        tripdto.setTripId(trip.getTripId());
        tripdto.setRiderId(trip.getUserEmail());
        tripdto.setBikeId(trip.getBike().getBikeId());
        tripdto.setStartTime(trip.getStartTime());

        if(trip.getEndTime() != null){
            tripdto.setEndTime(trip.getEndTime());
        }
        return tripdto;
    }

}
