package com.veloMTL.veloMTL.Service;

import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Repository.StationRepository;
import com.veloMTL.veloMTL.untils.Mappers.StationMapper;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    private final StationRepository stationRepo;

    public StationService(StationRepository repository) {
        this.stationRepo = repository;
    }

    public StationDTO createStation(StationDTO stationDTO) {
        Station newstation = StationMapper.dtoToEntity(stationDTO);
        Station savedStation = stationRepo.save(newstation);
        return StationMapper.entityToDto(savedStation);
    }

}
