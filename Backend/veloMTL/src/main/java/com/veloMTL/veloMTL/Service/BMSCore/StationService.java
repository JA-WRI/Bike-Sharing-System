package com.veloMTL.veloMTL.Service.BMSCore;

<<<<<<< HEAD
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Patterns.State.Stations.*;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Service.NotificationService;
import com.veloMTL.veloMTL.untils.Mappers.StationMapper;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
=======
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Repository.DockRepository;
import com.veloMTL.veloMTL.Repository.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.StationMapper;
import org.springframework.stereotype.Service;

>>>>>>> uroosa2
import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final DockRepository dockRepository;
<<<<<<< HEAD
    private final NotificationService notificationService;

    public StationService(StationRepository repository, DockRepository dockRepository, NotificationService notificationService) {
        this.stationRepository = repository;
        this.dockRepository = dockRepository;
        this.notificationService = notificationService;
    }

    public StationDTO createStation(StationDTO stationDTO) {
        Station newstation = StationMapper.dtoToEntity(stationDTO, new ArrayList<>());
=======

    public StationService(StationRepository repository, DockRepository dockRepository) {
        this.stationRepository = repository;
        this.dockRepository = dockRepository;
    }

    public StationDTO createStation(StationDTO stationDTO) {
        Station newstation = StationMapper.dtoToEntity(stationDTO);
>>>>>>> uroosa2
        Station savedStation = stationRepository.save(newstation);
        return StationMapper.entityToDto(savedStation);
    }

<<<<<<< HEAD
    public ResponseDTO<StationDTO> markStationOutOfService(String stationId){
        Station station = loadDockWithState(stationId);
        StateChangeResponse message = station.getStationState().markStationOutOfService(station);
        stationRepository.save(station);

        List<Dock> docks = station.getDocks();
        for(Dock dock: docks){
            dockRepository.save(dock);
        }
        return new ResponseDTO<>(message.getStatus(), message.getMessage(), StationMapper.entityToDto(station));
    }

    public ResponseDTO<StationDTO> restoreStation(String stationId){
        Station station = loadDockWithState(stationId);
        StateChangeResponse message = station.getStationState().restoreStation(station);
        stationRepository.save(station);

        List<Dock> docks = station.getDocks();
        for(Dock dock: docks){
            dockRepository.save(dock);
        }
        return new ResponseDTO<>(message.getStatus(), message.getMessage(), StationMapper.entityToDto(station));
    }

    private Station loadDockWithState(String stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Dock not found with ID: " + stationId));
        station.setStationState(createStateFromStatus(station.getStationStatus()));
        return station;
    }

    public void updateStationOccupancy(Station station, int newOccupancy) {
        station.setOccupancy(newOccupancy);

        if (newOccupancy == 0) {
            station.setStationStatus(StationStatus.EMPTY);
            station.setStationState(new EmptyStationState(notificationService));
            notificationService.notifyOperators("Station " + station.getStationName() + " is now EMPTY.");
        } else if (newOccupancy == station.getCapacity()) {
            station.setStationStatus(StationStatus.FULL);
            station.setStationState(new FullStationState(notificationService));
            notificationService.notifyOperators("Station " + station.getStationName() + " is now FULL.");
        } else {
            station.setStationStatus(StationStatus.OCCUPIED);
            station.setStationState(new OccupiedStationState());
        }

        stationRepository.save(station);
    }

    private StationState createStateFromStatus(StationStatus status) {
        return switch (status) {
            case EMPTY -> new EmptyStationState(notificationService);
            case FULL -> new FullStationState(notificationService);
            case OCCUPIED -> new OccupiedStationState();
            case OUT_OF_SERVICE -> new MaintenanceStationState(notificationService);
        };
    }
=======
    public StationDTO updateStationStatus(String stationId, StationStatus newStatus) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        // Update the station status
        station.setStationStatus(newStatus);
        stationRepository.save(station);

        // If station is OUT_OF_SERVICE, update all docks as well
        if (newStatus == StationStatus.OUT_OF_SERVICE) {
            List<Dock> docks = dockRepository.findByStationId(stationId);
            for (Dock dock : docks) {
                dock.setStatus(DockStatus.OUT_OF_SERVICE);
            }
            dockRepository.saveAll(docks);
        }
        Station updatedStation = stationRepository.save(station);
        return StationMapper.entityToDto(updatedStation);
    }



>>>>>>> uroosa2
}
