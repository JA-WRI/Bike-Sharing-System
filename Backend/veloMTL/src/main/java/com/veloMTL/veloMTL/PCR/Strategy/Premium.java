package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
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
    public double calculateTripCost(long tripDuration, boolean isEbike, double flexDollars, RiderRepository riderRepository, String riderId, int arrivalStationOccupancy) {
        if(tripDuration<1) tripDuration = 1;

        double tripCost = tripDuration * ratebyMinute;

        if (flexDollars >= tripCost) {
            flexDollars -= tripCost;
            tripCost = 0;
        } else {
            tripCost -= flexDollars;
            flexDollars = 0;
        }
        Rider rider = riderRepository.findById(riderId).orElseThrow(() -> new UsernameNotFoundException("Rider not found with id: " + riderId));
        rider.setFlexDollars(flexDollars);

        addFlexDollars(rider, arrivalStationOccupancy);

        return tripCost;

    }
    @Override
    public void addFlexDollars(Rider rider, int arrivalStationOccupancy){
        double flexDollars = rider.getFlexDollars();

        if(arrivalStationOccupancy<=25){
            flexDollars+=1;
            rider.setFlexDollars(flexDollars);
        }
    }
}
