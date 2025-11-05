package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;


@Service
public class BillingService {
    @Autowired
    private BillingRepository billingRepository;

    private long tripDuration (Trip trip){
        LocalDateTime startTime  = trip.getStartTime();
        LocalDateTime endTime = trip.getEndTime();
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
    }

    public Billing pay(Trip trip){
        Billing bill = null;
        Rider rider = trip.getRider();
        long tripDuration = tripDuration(trip);
        String bikeType = trip.getBike().getBikeType();
        boolean isEBike = false;

        if(bikeType.equalsIgnoreCase("e-Bike")){
            isEBike=true;
        }
        Plan plan = rider.getPlan();
        if(plan != null){
            double tripCost = plan.calculateTripCost(tripDuration, isEBike);
            LocalDateTime date = LocalDateTime.now();

            bill = new Billing(rider.getId(), "Trip", tripCost, date);
            billingRepository.save(bill);
        }

        return bill;
    }
    public Billing generateMonthlyBilling(Rider rider) {
        Plan plan = rider.getPlan();
        if (plan == null) return null;

        int baseFee = plan.getBaseFee();
        LocalDateTime date = LocalDateTime.now();

        Billing bill = new Billing(rider.getId(), "Monthly Base Fee", baseFee, date);
        billingRepository.save(bill);
        return bill;
    }



}
