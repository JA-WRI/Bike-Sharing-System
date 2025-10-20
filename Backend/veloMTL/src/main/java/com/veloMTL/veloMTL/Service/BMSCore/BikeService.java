package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Patterns.State.Bikes.*;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.BikeMapper;
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
        Bike bike = BikeMapper.dtoToEntity(bikeDTO, dock);

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
        dock.getStation().addBike();
        //save the station
        stationRepository.save(dock.getStation());
        return BikeMapper.entityToDto(bike);
    }

    public ResponseDTO<BikeDTO> unlockBike(String bikeId, String userId){
        Bike bike = loadDockWithState(bikeId);
        Dock dock = bike.getDock();
        Station station = dock.getStation();
        String message = bike.getState().unlockBike(bike,dock);

        station.removeBike();
        dock.setBike(null);
        bike.setDock(null);

        bikeRepository.save(bike);
        dockRepository.save(dock);
        stationRepository.save(station);

        return new ResponseDTO<>(true, message, BikeMapper.entityToDto(bike));
    }

    public ResponseDTO<BikeDTO> lockBike(String bikeId, String operatorId, String dockId){
        Bike bike = loadDockWithState(bikeId);
        Dock dock = dockRepository.findById(dockId).orElseThrow(() -> new RuntimeException("Dock not found with ID: " + dockId));
        Station station = dock.getStation();
        String message = bike.getState().lockBike(bike, dock);

        station.addBike();


        bikeRepository.save(bike);
        dockRepository.save(dock);
        stationRepository.save(station);

        return new ResponseDTO<>(true, message, BikeMapper.entityToDto(bike));
    }

    private Bike loadDockWithState(String bikeId) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new RuntimeException("Bike not found with ID: " + bikeId));
        bike.setState(createStateFromStatus(bike.getBikeStatus()));
        return bike;
    }

    private BikeState createStateFromStatus(BikeStatus status) {
        return switch (status) {
            case AVAILABLE -> new AvailableBikeState();
            case RESERVED -> new ReservedBikeState();
            case ON_TRIP -> new OnTripBikeState();
            case OUT_OF_SERVICE -> new MaintenanceBikeState();
        };
    }
}
