package com.veloMTL.veloMTL.utils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StationMapper {

    public static Station dtoToEntity(StationDTO dto, List<Dock> docks) {
        Station station = new Station();
        station.setId(dto.getId());
        station.setStreetAddress(dto.getStreetAddress());
        station.setStationName(dto.getStationName());
        station.setPosition(dto.getPosition());
        station.setStationStatus(dto.getStationStatus());
        station.setCapacity(dto.getCapacity());
        station.setDocks(docks);

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
        dto.setDocks(new ArrayList<>());
        dto.setOccupancy(station.getOccupancy());

        if (station.getDocks() != null) {
            List<Dock> docks = station.getDocks();
            Set<String> seenDockIds = new HashSet<>();
            for (Dock dock : docks) {
                if (!seenDockIds.contains(dock.getDockId())) {
                    dto.getDocks().add(DockMapper.entityToDto(dock));
                    seenDockIds.add(dock.getDockId());
                }
            }
        }
        return dto;
    }

}
