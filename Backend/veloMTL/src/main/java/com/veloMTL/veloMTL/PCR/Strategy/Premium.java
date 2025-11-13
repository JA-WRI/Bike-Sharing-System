package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class Premium implements Plan{
    private int baseFee = 20;
    private double ratebyMinute = 0.15;
    RiderRepository riderRepository;
    //private int eBikeCharge = 0;

    public Premium(){}

    public Premium(int baseFee, double ratebyMinute,RiderRepository riderRepository) {
        this.baseFee = baseFee;
        this.ratebyMinute = ratebyMinute;
    }
    public int getBaseFee() {return baseFee;}
    public void setBaseFee(int baseFee) {this.baseFee = baseFee;}
    public double getRatebyMinute() {return ratebyMinute;}
    public void setRatebyMinute(double ratebyMinute) {this.ratebyMinute = ratebyMinute;}

    @Override
    public double calculateTripCost(long tripDuration, boolean isEbike, double flexDollars, RiderRepository riderRepository,OperatorRepository operatorRepository, String userId, int arrivalStationOccupancy) {
        if(tripDuration<1) tripDuration = 1;

        double tripCost = tripDuration * ratebyMinute;

        if (flexDollars >= tripCost) {
            flexDollars -= tripCost;
            tripCost = 0;
        } else {
            tripCost -= flexDollars;
            flexDollars = 0;
        }
        User user = riderRepository.findById(userId).orElse(null);
        if (user == null) user = operatorRepository.findById(userId).orElseThrow(()-> new RuntimeException("User does not exist with id: "+ userId));


        user.setFlexDollars(flexDollars);

        addFlexDollars(user, arrivalStationOccupancy);

        return tripCost;

    }
    @Override
    public void addFlexDollars(User user, int arrivalStationOccupancy){
        double flexDollars = user.getFlexDollars();

        if(arrivalStationOccupancy<=25){
            flexDollars+=1;
            user.setFlexDollars(flexDollars);
        }
    }
}
