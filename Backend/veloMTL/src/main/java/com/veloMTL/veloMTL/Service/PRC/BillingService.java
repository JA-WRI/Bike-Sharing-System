package com.veloMTL.veloMTL.Service.PRC;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final RiderRepository riderRepository;
    private final OperatorRepository operatorRepository;
    private final StationRepository stationRepository;

    public BillingService(BillingRepository billingRepository, RiderRepository riderRepository, OperatorRepository operatorRepository,StationRepository stationRepository) {
        this.billingRepository = billingRepository;
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
        this.stationRepository = stationRepository;
    }

    private long getTripDurationInMinutes(Trip trip) {
        if (trip.getStartTime() == null || trip.getEndTime() == null) return 0;
        return Duration.between(trip.getStartTime(), trip.getEndTime()).toMinutes();
    }

    public Billing pay(Trip trip) {
        //declare var
        Billing bill = null;
        double tripCost = 0.0;

        //get user email
        String userEmail = trip.getUserEmail();

        //get user based on email
        User user = riderRepository.findByEmail(userEmail).orElse(null);
        if (user == null) user = operatorRepository.findByEmail(userEmail).orElseThrow(()-> new RuntimeException("User does not exist with email: "+ userEmail));

        //process bill if the user is found
        if (user != null) {
            //get user payment plan
            Plan plan = user.getPlan();

            //get user flexDollar balance
            double flexDollars = user.getFlexDollars();

            //get trip info for the billing
            LocalDateTime startDate = trip.getStartTime();
            LocalDateTime endDate = trip.getEndTime();

            String originStation = trip.getOriginStation();
            String arrivalStation = trip.getArrivalStation();

            String bikeId = trip.getBike().getBikeId();

            // get data from the trip to perform trip calculations
            int arrivalStationOccupancy = stationRepository.findByStationName(arrivalStation)
                    .orElseThrow(() -> new RuntimeException("Station not found: " + arrivalStation))
                    .getOccupancy();

            boolean isEBike = trip.getBike().getBikeType().equalsIgnoreCase("e-Bike");
            long tripDuration = getTripDurationInMinutes(trip);
            double ratePerMinute = plan.getRatebyMinute();

            if (user instanceof Operator) {
                tripCost = plan.calculateTripCost(tripDuration, isEBike, flexDollars,riderRepository,operatorRepository, user.getId(), arrivalStationOccupancy);
                tripCost = tripCost*(1-0.05);
            } else {
                tripCost = plan.calculateTripCost(tripDuration, isEBike, flexDollars, riderRepository,operatorRepository, user.getId(), arrivalStationOccupancy);
            }
            bill = new Billing("Trip", LocalDateTime.now(), user.getId(), bikeId, originStation, arrivalStation, startDate, endDate, ratePerMinute, tripCost);
            billingRepository.save(bill);
        }
        return bill;

    }

    public Billing generateMonthlyBillingRiders(Rider rider) {
        if (rider == null) return null;

        Plan plan = rider.getPlan();
        if (plan == null) return null;

        int baseFee = plan.getBaseFee();
        Billing bill = new Billing("Monthly Base Fee", LocalDateTime.now(), rider.getId(), baseFee);
        billingRepository.save(bill);

        return bill;
    }

    public Billing generateMonthlyBillingOperator(Operator operator) {
        if (operator == null) return null;

        Plan plan = operator.getPlan();
        if (plan == null) return null;

        int baseFee = plan.getBaseFee();
        Billing bill = new Billing("Monthly Base Fee", LocalDateTime.now(), operator.getId(), baseFee);
        billingRepository.save(bill);

        return bill;
    }

    public List<Billing> getAllRiderBilling(String userEmail) {
        User user = riderRepository.findByEmail(userEmail).orElse(null);
        if (user == null) user = operatorRepository.findByEmail(userEmail).orElse(null);

        return billingRepository.findAllByriderID(user.getId());
    }


}
