package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.DTO.Users.RiderDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.RiderRepository;
import com.veloMTL.veloMTL.untils.Mappers.RiderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public RiderDTO createRider(RiderDTO riderDTO) {
        // Add validation later (for login and registration)

        // Convert DTO to entity
        Rider rider = RiderMapper.dtoToEntity(riderDTO);
        if (rider.getPermissions() == null || rider.getPermissions().isEmpty()) {
            rider.setPermissions(List.of("RESERVE_DOCK", "UNLOCK_BIKE", "VIEW_STATION"));
        }

        // Save entity to DB
        Rider savedRider = riderRepository.save(rider);

        // Convert back to DTO and return
        return RiderMapper.entityToDto(savedRider);
    }
}
