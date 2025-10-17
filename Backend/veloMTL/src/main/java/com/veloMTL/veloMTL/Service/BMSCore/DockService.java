package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.Model.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Repository.DockRepository;
import com.veloMTL.veloMTL.Repository.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.DockMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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

   public void updateDockStatus(String dockId, DockStatus newStatus){
       System.out.println("Updating dock " + dockId + " to status " + newStatus);
        Dock dock = dockRepo.findById(dockId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dock not found: " + dockId));
       System.out.println("Updating dock " + dockId + " to status " + newStatus);
        dock.setStatus(newStatus);

        Dock dockSaved = dockRepo.save(dock);
//      return DockMapper.entityToDto(dockSaved);



   }



}
