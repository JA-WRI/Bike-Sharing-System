package com.veloMTL.veloMTL.utils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.ElectricBike;

public class BikeMapper {

    public static Bike dtoToEntity(BikeDTO bikeDTO, Dock dock) {

        Bike bike;
        if (bikeDTO.getBikeType().equalsIgnoreCase("regular")){
            //create the bike and assign the dock to bike
            bike = new Bike(bikeDTO.getBikId(), bikeDTO.getBikeType(), bikeDTO.getBikeStatus(), dock,
                    bikeDTO.getReserveDate(), bikeDTO.getReserveUser());

        } else {
            bike = new ElectricBike(bikeDTO.getBikId(), bikeDTO.getBikeType(), bikeDTO.getBikeStatus(), dock,
                    bikeDTO.getReserveDate(), bikeDTO.getReserveUser(),"100" );
        }

        return bike;
    }


    public static BikeDTO entityToDto(Bike bike) {
        BikeDTO dto = new BikeDTO();
        dto.setBikeStatus(bike.getBikeStatus());
        dto.setBikeType(bike.getBikeType());
        dto.setReserveDate(bike.getReserveDate());
        dto.setReserveUser(bike.getReserveUser());

        // Safely get dockId, handling lazy loading exceptions
        try {
            if(bike.getDock() == null) {
                dto.setDockId(null);
            } else {
                dto.setDockId(bike.getDock().getDockId());
            }
        } catch (Exception e) {
            // Handle lazy loading exception - dock reference couldn't be loaded
            dto.setDockId(null);
        }

        dto.setBikId(bike.getBikeId());

        return dto;
    }
    
    // Overloaded method that accepts dockId to avoid lazy loading
    public static BikeDTO entityToDto(Bike bike, String dockId) {
        BikeDTO dto = entityToDto(bike);
        if (dockId != null) {
            dto.setDockId(dockId);
        }
        return dto;
    }

}
