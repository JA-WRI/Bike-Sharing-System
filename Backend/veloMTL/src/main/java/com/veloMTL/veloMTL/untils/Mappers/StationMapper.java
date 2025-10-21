package com.veloMTL.veloMTL.untils.Mappers;

<<<<<<< HEAD
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;

import java.util.ArrayList;
import java.util.List;

public class StationMapper {

    public static Station dtoToEntity(StationDTO dto, List<Dock> docks) {
=======
import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationMapper {
    public static Station dtoToEntity(StationDTO dto) {
>>>>>>> uroosa2
        Station station = new Station();
        station.setId(dto.getId());
        station.setStreetAddress(dto.getStreetAddress());
        station.setStationName(dto.getStationName());
        station.setPosition(dto.getPosition());
        station.setStationStatus(dto.getStationStatus());
        station.setCapacity(dto.getCapacity());
<<<<<<< HEAD
        station.setDocks(docks);

=======
        // docks will be added separately in DockService
>>>>>>> uroosa2
        return station;
    }

    public static StationDTO entityToDto(Station station) {
        StationDTO dto = new StationDTO();
        dto.setId(station.getId());
        dto.setStationName(station.getStationName());
        dto.setStreetAddress(station.getStreetAddress());
        dto.setPosition(station.getPosition());
        dto.setStationStatus(station.getStationStatus());
        dto.setCapacity(station.getCapacity());
<<<<<<< HEAD
        dto.setDocks(new ArrayList<>());

        if (station.getDocks() != null) {
            List<Dock> docks = station.getDocks();
            for (Dock dock: docks){
                dto.getDocks().add(dock.getDockId());
            }
        }
=======

        if (station.getDocks() != null) {
            List<DockDTO> dockDTOs = station.getDocks().stream()
                    .map(DockMapper :: entityToDto)
                    .collect(Collectors.toList());
            dto.setDocks(dockDTOs);
        }

>>>>>>> uroosa2
        return dto;
    }

}
