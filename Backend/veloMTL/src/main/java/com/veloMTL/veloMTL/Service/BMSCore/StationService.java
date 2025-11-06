package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Stations.*;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.utils.Mappers.StationMapper;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final DockRepository dockRepository;
    private final NotificationService notificationService;

    public StationService(StationRepository repository, DockRepository dockRepository, NotificationService notificationService) {
        this.stationRepository = repository;
        this.dockRepository = dockRepository;
        this.notificationService = notificationService;
    }

    public StationDTO createStation(StationDTO stationDTO) {
        Station newstation = StationMapper.dtoToEntity(stationDTO, new ArrayList<>());
        Station savedStation = stationRepository.save(newstation);
        return StationMapper.entityToDto(savedStation);
    }

    public ResponseDTO<StationDTO> markStationOutOfService(String stationId, UserStatus role){
        Station station = loadDockWithState(stationId);
        StateChangeResponse message = station.getStationState().markStationOutOfService(station);
        stationRepository.save(station);

        List<Dock> docks = station.getDocks();
        for(Dock dock: docks){
            dockRepository.save(dock);
        }
        return new ResponseDTO<>(message.getStatus(), message.getMessage(), StationMapper.entityToDto(station));
    }

    public ResponseDTO<StationDTO> restoreStation(String stationId, UserStatus role){
        Station station = loadDockWithState(stationId);
        StateChangeResponse message = station.getStationState().restoreStation(station);
        stationRepository.save(station);

        List<Dock> docks = station.getDocks();
        for(Dock dock: docks){
            dockRepository.save(dock);
        }
        return new ResponseDTO<>(message.getStatus(), message.getMessage(), StationMapper.entityToDto(station));
    }

    //Helper methods
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

    private Station loadDockWithState(String stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Dock not found with ID: " + stationId));
        station.setStationState(createStateFromStatus(station.getStationStatus()));
        return station;
    }

    private StationState createStateFromStatus(StationStatus status) {
        return switch (status) {
            case EMPTY -> new EmptyStationState(notificationService);
            case FULL -> new FullStationState(notificationService);
            case OCCUPIED -> new OccupiedStationState();
            case OUT_OF_SERVICE -> new MaintenanceStationState(notificationService);
        };
    }

    public StationDTO getStationById(String stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found with ID: " + stationId));
        return StationMapper.entityToDto(station);
    }
}
