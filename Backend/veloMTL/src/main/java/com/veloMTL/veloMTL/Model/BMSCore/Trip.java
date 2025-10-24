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

@Document(collection = "Trips")
@TypeAlias("trip")
public class Trip {
    @Id
    private String tripId;
    private String startTime;
    private String endTime;

    @DBRef(lazy = true)
    private Bike bike;
    @DBRef(lazy = true)
    private Rider rider;

//    @Transient
//    private BikeState state;


    public Trip(String tripId, String startTime, String endTime, Bike bike, Rider rider) {
        this.tripId = tripId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bike = bike;
        this.rider = rider;
    }

    public String getTripId() {
        return tripId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Bike getBike() {
        return this.bike;
    }

    public Rider getRider() {
        return this.rider;
    }
}
