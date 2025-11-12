package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
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
    public double calculateTripCostRider(long tripDuration, boolean isEbike,double flexDollars,RiderRepository riderRepository,String riderId, int arrivalStationOccupancy) {

        if(tripDuration<1) tripDuration = 1;

        double tripCost = tripDuration * ratebyMinute;
        if(isEbike){
            tripCost+=eBikeCharge;
        }
        if (flexDollars >= tripCost) {
            flexDollars -= tripCost;
            tripCost = 0;
        } else {
            tripCost -= flexDollars;
            flexDollars = 0;
        }
        Rider rider = riderRepository.findById(riderId).orElseThrow(() -> new UsernameNotFoundException("Rider not found with id: " + riderId));
        rider.setFlexDollars(flexDollars);

        addFlexDollarsRider(rider, arrivalStationOccupancy);

        return tripCost;
    }
    @Override
    public void addFlexDollarsRider (Rider rider, int arrivalStationOccupancy){
        double flexDollars = rider.getFlexDollars();

        if(arrivalStationOccupancy<=25){
           flexDollars+=1;
           rider.setFlexDollars(flexDollars);
        }

    }
@Override
public double calculateTripCostOperator(long tripDuration, boolean isEbike, double flexDollars, OperatorRepository operatorRepository, String operatorId, int arrivalStationOccupancy){
 return 1;
}


}
