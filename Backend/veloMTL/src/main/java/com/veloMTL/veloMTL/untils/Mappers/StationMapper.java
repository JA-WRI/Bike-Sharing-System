package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationMapper {
    public static Station dtoToEntity(StationDTO dto) {
        Station station = new Station();
        station.setId(dto.getId());
        station.setStreetAddress(dto.getStreetAddress());
        station.setStationName(dto.getStationName());
        station.setPosition(dto.getPosition());
        station.setStationStatus(dto.getStationStatus());
        station.setCapacity(dto.getCapacity());
        // docks will be added separately in DockService
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

        if (station.getDocks() != null) {
            List<DockDTO> dockDTOs = station.getDocks().stream()
                    .map(DockMapper :: entityToDto)
                    .collect(Collectors.toList());
            dto.setDocks(dockDTOs);
        }

        return dto;
    }

}
