package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;

public class DockMapper {
    public static Dock dtoToEntity(DockDTO dto, Station station) {
        Dock dock = new Dock();
        dock.setDockId(dto.getDockId());
        dock.setStatus(dto.getStatus());
        dock.setStation(station);
        dock.setReserveDate(dto.getReserveDate());
        dock.setReserveUser(dto.getReserveUser());
        return dock;
    }

    public static DockDTO entityToDto(Dock dock) {
        DockDTO dto = new DockDTO();
        dto.setDockId(dock.getDockId());
        dto.setStatus(dock.getStatus());
        dto.setStationId(dock.getStation() != null ? dock.getStation().getId() : null);
        dto.setReserveUser(dock.getReserveUser());
        dto.setReserveDate(dock.getReserveDate());
        return dto;
    }

}
