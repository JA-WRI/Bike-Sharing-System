package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.State.Bikes.*;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.untils.Mappers.BikeMapper;
import com.veloMTL.veloMTL.untils.Mappers.TripMapper;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;
import org.springframework.stereotype.Service;


@Service
public class TripService {
    private final BikeRepository bikeRepository;
    private final DockRepository dockRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final RiderRepository riderRepository;
    private final TripRepository tripRepository;

    public TripService(BikeRepository bikeRepository, DockRepository dockRepository, StationRepository stationRepository, StationService stationService, RiderRepository riderRepository, TripRepository tripRepository) {
        this.bikeRepository = bikeRepository;
        this.dockRepository = dockRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.riderRepository = riderRepository;
        this.tripRepository = tripRepository;
    }

    //can change this later if needed
    public TripDTO createTrip(TripDTO tripDTO){
        //Find the bike
        Bike bike = bikeRepository.findById(tripDTO.getBikeId()).orElseThrow(() -> new RuntimeException("Bike not found"));;
        //Find the rider
        Rider rider = riderRepository.findById(tripDTO.getRiderId()).orElseThrow(() -> new RuntimeException("Rider not found"));;

        Trip trip = TripMapper.dtoToEntity(tripDTO, bike, rider);

        //save the trip
        tripRepository.save(trip);

        return TripMapper.entityToDto(trip);
    }
}
