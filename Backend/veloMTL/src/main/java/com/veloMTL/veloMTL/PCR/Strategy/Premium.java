package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;

public class Premium implements Plan{
    private int baseFee = 20;
    private double ratebyMinute = 0.05;
    //private int eBikeCharge = 0;

    public Premium(){}

    public Premium(int baseFee, double ratebyMinute) {
        this.baseFee = baseFee;
        this.ratebyMinute = ratebyMinute;
    }
    public int getBaseFee() {return baseFee;}
    public void setBaseFee(int baseFee) {this.baseFee = baseFee;}
    public double getRatebyMinute() {return ratebyMinute;}
    public void setRatebyMinute(double ratebyMinute) {this.ratebyMinute = ratebyMinute;}

    @Override
    public double calculateTripCost(long tripDuration, boolean isEbike) {
        return tripDuration*ratebyMinute;

    }
}
