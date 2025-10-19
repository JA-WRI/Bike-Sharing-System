package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.ElectricBike;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private final DockRepository dockRepository;
    private final StationRepository stationRepository;

    public BikeService(BikeRepository bikeRepository, DockRepository dockRepository, StationRepository stationRepository) {
        this.bikeRepository = bikeRepository;
        this.dockRepository = dockRepository;
        this.stationRepository = stationRepository;
    }

    //can change this later if needed
    public BikeDTO createBike(BikeDTO bikeDTO){
        //Find the dock
        Dock dock = dockRepository.findById(bikeDTO.getDockId()).orElseThrow(() -> new RuntimeException("Dock not found"));;

        Bike bike;
        if (bikeDTO.getBikeType().equalsIgnoreCase("regular")){
            //create the bike and assign the dock to bike
            bike = new Bike(bikeDTO.getBikId(), bikeDTO.getBikeType(), bikeDTO.getBikeStatus(), dock);

        } else {
            bike = new ElectricBike(bikeDTO.getBikId(), bikeDTO.getBikeType(), bikeDTO.getBikeStatus(), dock, "100");
        }
        //save the bike
        bikeRepository.save(bike);
        //update the dock status
        dock.setStatus(DockStatus.OCCUPIED);
        //assign the bike to the dock
        dock.setBike(bike);
        //save the dock
        dockRepository.save(dock);
        //change the station status
        dock.getStation().setStationStatus(StationStatus.OCCUPIED);
        //save the station
        stationRepository.save(dock.getStation());
        return new BikeDTO(bike.getBikeId(), bike.getBikeType(), bike.getDock().getDockId(), bike.getBikeStatus());
    }

    //reserve a bike
    //Unlock bike
    //Lock bike
    //start trip
}
