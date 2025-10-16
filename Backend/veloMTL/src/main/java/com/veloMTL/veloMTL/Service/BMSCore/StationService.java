package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Repository.DockRepository;
import com.veloMTL.veloMTL.Repository.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.StationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final DockRepository dockRepository;

    public StationService(StationRepository repository, DockRepository dockRepository) {
        this.stationRepository = repository;
        this.dockRepository = dockRepository;
    }

    public StationDTO createStation(StationDTO stationDTO) {
        Station newstation = StationMapper.dtoToEntity(stationDTO);
        Station savedStation = stationRepository.save(newstation);
        return StationMapper.entityToDto(savedStation);
    }

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



}
