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

//    public Trip(Bike bike, Operator operator) {
//        this.bike = bike;
//        this.operator = operator;
//    }

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
//    public void setBike(Bike bike) {this.bike = bike;}
//    public Rider getRider() {return rider;}
//    public void setRider(Rider rider) {
//        this.rider = rider;
//        if (rider != null) {
//            this.operator = null; // Ensure only one is set
//        }
//    }
//    public Operator getOperator() {return operator;}
//    public void setOperator(Operator operator) {
//        this.operator = operator;
//        if (operator != null) {
//            this.rider = null; // Ensure only one is set
//        }
//    }

//    /**
//     * Gets the user ID regardless of whether it's a Rider or Operator
//     */
//    public String getUserId() {
//        if (rider != null) {
//            return rider.getId();
//        } else if (operator != null) {
//            return operator.getId();
//        }
//        return null;
//    }
//
//    /**
//     * Gets the user status (RIDER or OPERATOR) based on which field is set
//     */
//    public UserStatus getUserStatus() {
//        if (rider != null) {
//            return UserStatus.RIDER;
//        } else if (operator != null) {
//            return UserStatus.OPERATOR;
//        }
//        return null;
//    }

    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public String getOriginStation() {return originStation;}
    public void setOriginStation(String originStation) {this.originStation = originStation;}
    public String getArrivalStation() {return arrivalStation;}
    public void setArrivalStation(String arrivalStation) {this.arrivalStation = arrivalStation;}
    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }
}
