package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.ElectricBike;
import com.veloMTL.veloMTL.Model.BMSCore.Station;

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

        if(bike.getDock() == null)
            dto.setDockId(null);
        else
            dto.setDockId(bike.getDock().getDockId());

        dto.setBikId(bike.getBikeId());

        return dto;
    }

}
