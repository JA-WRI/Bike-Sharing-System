package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;

@TypeAlias("e-Bike")
public class ElectricBike extends Bike{
    private String battery;

    public ElectricBike() {
        super();
    }

    public ElectricBike(String bikeId, String bikeType, BikeStatus bikeStatus, Dock dock, String battery) {
        super(bikeId, bikeType, bikeStatus, dock);
        this.battery = battery;
    }

    public ElectricBike(String bikeId, String bikeType, BikeStatus bikeStatus, Dock dock, LocalDateTime reserveDate,
                        String reserveUser, String battery) {
        super(bikeId, bikeType, bikeStatus, dock, reserveDate, reserveUser);
        this.battery = battery;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }
}
