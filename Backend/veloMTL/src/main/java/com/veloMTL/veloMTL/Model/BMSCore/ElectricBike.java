package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias("electricBike")
public class ElectricBike extends Bike{
    private String battery;

    public ElectricBike(String bikeId, String bikeType, BikeStatus bikeStatus, Dock dock, String battery) {
        super(bikeId, bikeType, bikeStatus, dock);
        this.battery = battery;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }
}
