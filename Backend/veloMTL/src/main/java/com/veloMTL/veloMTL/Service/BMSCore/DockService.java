package com.veloMTL.veloMTL.Service.BMSCore;

<<<<<<< HEAD
import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Patterns.State.Docks.*;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.DockMapper;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;
import org.springframework.stereotype.Service;
=======
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
>>>>>>> uroosa2


@Service
public class DockService {
    private final DockRepository dockRepo;
    private final StationRepository stationRepo;

    public DockService(DockRepository dockRepo, StationRepository stationRepo){
        this.dockRepo = dockRepo;
        this.stationRepo = stationRepo;
    }

<<<<<<< HEAD
   public DockDTO createDock(DockDTO dockDTO){
        Station station = stationRepo.findById(dockDTO.getStationId()).orElseThrow(() -> new RuntimeException("Station not found"));
        Dock dock = new Dock(dockDTO.getDockId(),station);

        Dock savedDock = dockRepo.save(dock);
        station.getDocks().add(savedDock);
        stationRepo.save(station);

        return new DockDTO(savedDock.getDockId(), savedDock.getStatus(), savedDock.getStation().getId(), null);
   }

   public ResponseDTO<DockDTO> reserveDock(String dockId){
        Dock dock = loadDockWithState(dockId);
        StateChangeResponse message = dock.getState().reserveDock(dock);
        dockRepo.save(dock);
        return new ResponseDTO<>(message.getStatus(),message.getMessage(),DockMapper.entityToDto(dock));
   }

   public ResponseDTO<DockDTO> markDockOutOfService(String dockId){
       Dock dock = loadDockWithState(dockId);
       StateChangeResponse message = dock.getState().markDockOutOfService(dock);
       dockRepo.save(dock);
       return new ResponseDTO<>(message.getStatus(),message.getMessage(),DockMapper.entityToDto(dock));
   }

    public ResponseDTO<DockDTO> restoreDockStatus(String dockId){
        Dock dock = loadDockWithState(dockId);
        StateChangeResponse message = dock.getState().restoreService(dock);
        dockRepo.save(dock);
        return new ResponseDTO<>(message.getStatus(),message.getMessage(),DockMapper.entityToDto(dock));
    }

    private Dock loadDockWithState(String dockId) {
        Dock dock = dockRepo.findById(dockId)
                .orElseThrow(() -> new RuntimeException("Dock not found with ID: " + dockId));
        dock.setState(createStateFromStatus(dock.getStatus()));
        return dock;
    }

    private DockState createStateFromStatus(DockStatus status) {
        return switch (status) {
            case EMPTY -> new EmptyDockState();
            case RESERVED -> new ReservedDockState();
            case OCCUPIED -> new OccupiedDockState();
            case OUT_OF_SERVICE -> new MaintenanceDockState();
        };
    }
=======
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

   public DockDTO updateDockStatus(String dockId, DockStatus newStatus){
       System.out.println("Updating dock " + dockId + " to status " + newStatus);
        Dock dock = dockRepo.findById(dockId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dock not found: " + dockId));
       System.out.println("Updating dock " + dockId + " to status " + newStatus);
        dock.setStatus(newStatus);

        Dock dockSaved = dockRepo.save(dock);
        return DockMapper.entityToDto(dockSaved);



   }



>>>>>>> uroosa2
}
