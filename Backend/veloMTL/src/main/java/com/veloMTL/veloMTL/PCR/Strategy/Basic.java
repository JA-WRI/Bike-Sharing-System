package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;

public class Basic implements  Plan{
    private int baseFee = 15;
    private double ratebyMinute = 0.20;
    private int eBikeCharge = 5;
    private Station station;
    private RiderRepository riderRepository;

    public Basic(){}

    public Basic(int baseFee, double ratebyMinute, int eBikeCharge,RiderRepository riderRepository) {
        this.baseFee = baseFee;
        this.ratebyMinute = ratebyMinute;
        this.eBikeCharge = eBikeCharge;
    }

    public int getBaseFee() {return baseFee;}
    public void setBaseFee(int baseFee) {this.baseFee = baseFee;}
    public double getRatebyMinute() {return ratebyMinute;}
    public void setRatebyMinute(double ratebyMinute) {this.ratebyMinute = ratebyMinute;}
    public int geteBikeCharge() {return eBikeCharge;}
    public void seteBikeCharge(int eBikeCharge) {this.eBikeCharge = eBikeCharge;}

    @Override
    public double calculateTripCost(long tripDuration, boolean isEbike, double flexDollars,
                                    RiderRepository riderRepository, OperatorRepository operatorRepository,
                                    String userId, int arrivalStationOccupancy) {

        if (tripDuration < 1) tripDuration = 1;

        double tripCost = tripDuration * ratebyMinute;

        if (isEbike) {
            tripCost += eBikeCharge;
        }

        // Deduct flex dollars
        if (flexDollars >= tripCost) {
            flexDollars -= tripCost;
            tripCost = 0;
        } else {
            tripCost -= flexDollars;
            flexDollars = 0;
        }

        // Load user
        User user = riderRepository.findById(userId).orElse(null);
        if (user == null)
            user = operatorRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User does not exist with id: " + userId));

        // Update flex balance
        user.setFlexDollars(flexDollars);

        // Add reward if eligible
        addFlexDollars(user, arrivalStationOccupancy);

        // REQUIRED — persist user
        if (user instanceof Rider) {
            riderRepository.save((Rider) user);
        } else {
            operatorRepository.save((Operator) user);
        }

        return tripCost;
    }

    @Override
    public void addFlexDollars(User user, int arrivalStationOccupancy) {
        double flexDollars = user.getFlexDollars();

        if (arrivalStationOccupancy <= 25) {
            flexDollars += 1;
            user.setFlexDollars(flexDollars);
        }
    }


}

