package com.veloMTL.veloMTL.Service;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.Model.Dock;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Repository.DockRepository;
import com.veloMTL.veloMTL.Repository.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.DockMapper;
import org.springframework.stereotype.Service;


@Service
public class DockService {
    private final DockRepository dockRepo;
    private final StationRepository stationRepo;

    public DockService(DockRepository dockRepo, StationRepository stationRepo){
        this.dockRepo = dockRepo;
        this.stationRepo = stationRepo;
    }

    // Create a new dock and link it to a station
   public DockDTO createDock(String stationId, DockDTO dockDTO){
        Station station = stationRepo.findById(stationId).orElseThrow(() -> new RuntimeException("Station not found"));
        Dock dock = DockMapper.dtoToEntity(dockDTO);
        dock.setStation(station);

        Dock savedDock = dockRepo.save(dock);

        station.getDocks().add(savedDock);
        stationRepo.save(station);

        return DockMapper.entityToDto(savedDock);
   }



}
