package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.State.Bikes.BikeState;
import com.veloMTL.veloMTL.Patterns.State.Docks.DockState;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Trips")
@TypeAlias("trip")
public class Trip {
    @Id
    private String tripId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @DBRef(lazy = true)
    private Bike bike;
    @DBRef(lazy = true)
    private Rider rider;

//    @Transient
//    private BikeState state;


    public Trip(String tripId, LocalDateTime startTime, LocalDateTime endTime, Bike bike, Rider rider) {
        this.tripId = tripId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bike = bike;
        this.rider = rider;
    }

    public String getTripId() {
        return tripId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Bike getBike() {
        return this.bike;
    }

    public Rider getRider() {
        return this.rider;
    }
}
