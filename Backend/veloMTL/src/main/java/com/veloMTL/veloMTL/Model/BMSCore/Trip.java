package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Patterns.State.Bikes.BikeState;
import com.veloMTL.veloMTL.Patterns.State.Docks.DockState;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
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
    private LocalDateTime reserveStart;
    private LocalDateTime reserveEnd;
    private boolean reservationExpired;

    @DBRef(lazy = true)
    private Bike bike;
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

    public LocalDateTime getReserveStart() {
        return this.reserveStart;
    }

    public void setReserveStart(LocalDateTime reserveStart) {
        this.reserveStart = reserveStart;
    }

    public LocalDateTime getReserveEnd() {
        return this.reserveEnd;
    }

    public void setReserveEnd(LocalDateTime reserveEnd) {
        this.reserveEnd = reserveEnd;
    }

    public boolean isReservationExpired() {
        return this.reservationExpired;
    }

    public void setReservationExpired(boolean flag) {
        this.reservationExpired = flag;
    }
}
