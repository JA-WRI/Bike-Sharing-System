package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.Model.Dock;

public class DockMapper {

    public static Dock dtoToEntity(DockDTO dto) {
        Dock dock = new Dock();
        dock.setId(dto.getId()); // can be null; Mongo will generate if so
        dock.setStatus(dto.getStatus());
        // station reference is set in the service
        return dock;
    }

    public static DockDTO entityToDto(Dock dock) {
        DockDTO dto = new DockDTO();
        dto.setId(dock.getId());
        dto.setStatus(dock.getStatus());
        dto.setStationId(dock.getStation() != null ? dock.getStation().getId() : null);
        return dto;
    }
}
