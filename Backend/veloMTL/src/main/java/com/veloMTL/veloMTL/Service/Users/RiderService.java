package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.stereotype.Service;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }
}
