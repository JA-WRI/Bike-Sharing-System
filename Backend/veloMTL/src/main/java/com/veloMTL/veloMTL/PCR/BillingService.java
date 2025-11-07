package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final RiderRepository riderRepository;
    private final TripRepository tripRepository;

    public BillingService(BillingRepository billingRepository, RiderRepository riderRepository, TripRepository tripRepository) {
        this.billingRepository = billingRepository;
        this.riderRepository = riderRepository;
        this.tripRepository = tripRepository;
    }

    private long getTripDurationInMinutes(Trip trip){
        if(trip.getStartTime() == null || trip.getEndTime() == null) return 0;
        return Duration.between(trip.getStartTime(), trip.getEndTime()).toMinutes();
    }

    public Billing pay(String tripId){

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Rider rider = trip.getRider();
        Plan plan = rider.getPlan();

        LocalDateTime startDate = trip.getStartTime();
        LocalDateTime endDate = trip.getEndTime();
        String bikeId = trip.getBike().getBikeId();
        String originStation = trip.getOriginStation();
        String arrivalStation = trip.getArrivalStation();

        if(plan == null) return null;
        boolean isEBike = trip.getBike().getBikeType().equalsIgnoreCase("e-Bike");
        long tripDuration = getTripDurationInMinutes(trip);

        double ratePerMinute = plan.getRatebyMinute();
        double tripCost = plan.calculateTripCost(tripDuration, isEBike);


        Billing bill = new Billing("Trip", LocalDateTime.now(),rider.getId(),bikeId,originStation,arrivalStation, startDate,endDate,ratePerMinute,tripCost);
        billingRepository.save(bill);

        return bill;

    }
    // Charge a rider the monthly base fee
    public Billing generateMonthlyBilling(Rider rider) {
        if(rider == null) return null;

        Plan plan = rider.getPlan();
        if(plan == null) return null;

        int baseFee = plan.getBaseFee();
        Billing bill = new Billing("Monthly Base Fee",LocalDateTime.now(),rider.getId(),  baseFee);
        billingRepository.save(bill);

        return bill;
    }


    public List<Billing> getAllRiderBilling(String riderEmail){
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found"));
        return billingRepository.findAllByriderID(rider.getId());
    }


}
