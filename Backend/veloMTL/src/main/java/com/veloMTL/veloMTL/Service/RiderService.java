package com.veloMTL.veloMTL.Service;

import com.veloMTL.veloMTL.DTO.RiderDTO;
import com.veloMTL.veloMTL.Model.Rider;
import com.veloMTL.veloMTL.Repository.RiderRepository;
import org.springframework.stereotype.Service;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public RiderDTO createRider(RiderDTO riderDTO) {
        // Add validation later (for login and registration)

        // Convert DTO to entity
        Rider rider = new Rider(riderDTO.getName(), riderDTO.getEmail());

        // Save entity to DB
        Rider savedRider = riderRepository.save(rider);

        // Convert back to DTO and return
        return new RiderDTO(savedRider);
    }
}
