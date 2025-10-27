package com.veloMTL.veloMTL.utils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.*;
import com.veloMTL.veloMTL.Model.Users.Rider;

public class TripMapper {

    public static Trip dtoToEntity(TripDTO tripDTO, Bike bike, Rider rider) {

        Trip trip;
//        if (bikeDTO.getBikeType().equalsIgnoreCase("regular")){
//            //create the bike and assign the dock to bike
//            bike = new Bike(bikeDTO.getBikId(), bikeDTO.getBikeType(), bikeDTO.getBikeStatus(), dock);
//
//        } else {
//            bike = new ElectricBike(bikeDTO.getBikId(), bikeDTO.getBikeType(), bikeDTO.getBikeStatus(), dock, "100");
//        }

        return new Trip(tripDTO.getTripId(), tripDTO.getStartTime(), tripDTO.getEndTime(), bike, rider);
    }


    public static TripDTO entityToDto(Trip trip) {
        return new TripDTO(trip.getTripId(), trip.getStartTime(), trip.getEndTime(), trip.getBike().getBikeId(), trip.getRider().getId());

//        if(bike.getDock() == null)
//            dto.setDockId(null);
//        else
//            dto.setDockId(bike.getDock().getDockId());
//
//        dto.setBikId(bike.getBikeId());

//        return dto;
    }

}
