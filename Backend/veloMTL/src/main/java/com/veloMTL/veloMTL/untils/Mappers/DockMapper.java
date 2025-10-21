package com.veloMTL.veloMTL.untils.Mappers;

<<<<<<< HEAD
import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;

public class DockMapper {
    public static Dock dtoToEntity(DockDTO dto, Station station) {
        Dock dock = new Dock();
        dock.setDockId(dto.getDockId());
        dock.setStatus(dto.getStatus());
        dock.setStation(station);
=======
import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.Model.Dock;

public class DockMapper {

    public static Dock dtoToEntity(DockDTO dto) {
        Dock dock = new Dock();
        dock.setId(dto.getId());
        dock.setStatus(dto.getStatus());
>>>>>>> uroosa2
        return dock;
    }

    public static DockDTO entityToDto(Dock dock) {
        DockDTO dto = new DockDTO();
<<<<<<< HEAD
        dto.setDockId(dock.getDockId());
=======
        dto.setId(dock.getId());
>>>>>>> uroosa2
        dto.setStatus(dock.getStatus());
        dto.setStationId(dock.getStation() != null ? dock.getStation().getId() : null);
        return dto;
    }
<<<<<<< HEAD

=======
>>>>>>> uroosa2
}
