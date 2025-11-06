package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class BillingService {
    @Autowired
    private BillingRepository billingRepository;

    private long getTripDurationInMinutes(Trip trip){
        if(trip.getStartTime() == null || trip.getEndTime() == null) return 0;
        return Duration.between(trip.getStartTime(), trip.getEndTime()).toMinutes();
    }

    public Billing pay(Trip trip){
        if(trip == null || trip.getRider() == null || trip.getBike() == null) return null;

        Rider rider = trip.getRider();
        Plan plan = rider.getPlan();
        if(plan == null) return null;

        boolean isEBike = trip.getBike().getBikeType().equalsIgnoreCase("e-Bike");
        long tripDuration = getTripDurationInMinutes(trip);

        double tripCost = plan.calculateTripCost(tripDuration, isEBike);

        Billing bill = new Billing(rider.getId(), "Trip", tripCost, LocalDateTime.now(),trip);
        billingRepository.save(bill);

        return bill;

    }
    // Charge a rider the monthly base fee
    public Billing generateMonthlyBilling(Rider rider) {
        if(rider == null) return null;

        Plan plan = rider.getPlan();
        if(plan == null) return null;

        int baseFee = plan.getBaseFee();
        Billing bill = new Billing(rider.getId(), "Monthly Base Fee", baseFee, LocalDateTime.now());
        billingRepository.save(bill);

        return bill;
    }

    public List<Billing> getAllRiderBilling(String riderID){
        return billingRepository.findAllByRiderID(riderID);
    }


}
