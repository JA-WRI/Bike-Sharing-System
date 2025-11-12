package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;

import java.time.Duration;
import java.time.LocalDateTime;

public class Basic implements  Plan{
    private int baseFee = 15;
    private double ratebyMinute = 0.10;
    private int eBikeCharge = 5;

    public Basic(){}

    public Basic(int baseFee, double ratebyMinute, int eBikeCharge) {
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
    public double calculateTripCost(long tripDuration, boolean isEbike) {
        if(tripDuration<1) tripDuration = 1;

        double tripCost = tripDuration * ratebyMinute;
        if(isEbike){
            tripCost+=eBikeCharge;
        }
            return tripCost;
    }
}
