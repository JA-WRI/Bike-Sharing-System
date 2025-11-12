package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.PCR.Billing;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Trips")
public class Trip {
    @Id
    private String tripId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String originStation;
    private String arrivalStation;

    @DBRef(lazy = true)
    private Bike bike;
    @DBRef(lazy = true)
    private String userEmail;
    @DBRef (lazy = true)
    private Billing billing;

    public Trip(){}

    public Trip(Bike bike, String userEmail) {
        this.bike = bike;
        this.userEmail = userEmail;
    }

    public String getTripId() {
        return tripId;
    }
    public void setTripId(String tripId) {
        this.tripId = tripId;
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
        return bike;
    }
    public void setBike(Bike bike) {this.bike = bike;}
    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public String getOriginStation() {return originStation;}
    public void setOriginStation(String originStation) {this.originStation = originStation;}
    public String getArrivalStation() {return arrivalStation;}
    public void setArrivalStation(String arrivalStation) {this.arrivalStation = arrivalStation;}
    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }
}
