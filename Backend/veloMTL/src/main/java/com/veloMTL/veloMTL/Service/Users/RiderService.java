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
}
