package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.ResponseDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Patterns.State.Stations.*;
import com.veloMTL.veloMTL.Repository.DockRepository;
import com.veloMTL.veloMTL.Repository.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.StationMapper;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository repository) {
        this.stationRepository = repository;
    }

    public StationDTO createStation(StationDTO stationDTO) {
        Station newstation = StationMapper.dtoToEntity(stationDTO);
        Station savedStation = stationRepository.save(newstation);
        return StationMapper.entityToDto(savedStation);
    }

    public ResponseDTO<StationDTO> markStationOutOfService(String stationId){
        Station station = loadDockWithState(stationId);
        String message = station.getStationState().markStationOutOfService(station);
        stationRepository.save(station);
        return new ResponseDTO<>(true, message, StationMapper.entityToDto(station));
    }

    public ResponseDTO<StationDTO> restoreStation(String stationId){
        Station station = loadDockWithState(stationId);
        String message = station.getStationState().restoreStation(station);
        stationRepository.save(station);
        return new ResponseDTO<>(true, message, StationMapper.entityToDto(station));
    }

    private Station loadDockWithState(String stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Dock not found with ID: " + stationId));
        station.setStationState(createStateFromStatus(station.getStationStatus()));
        return station;
    }

    private StationState createStateFromStatus(StationStatus status) {
        return switch (status) {
            case EMPTY -> new EmptyStationState();
            case FULL -> new FullStationState();
            case OCCUPIED -> new OccupiedStationState();
            case OUT_OF_SERVICE -> new MaintenanceStationState();
        };
    }
}
