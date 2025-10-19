package com.veloMTL.veloMTL.Model.Users;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "Riders")
public class Rider extends User{

    private String paymentInfo; //this should be turned into a PaymentInfo object
    private String reservationId; //only if a user reserves a bike

    public Rider() {
        super();
        this.setRole("RIDER");
    }

    public Rider(String name, String email, String password) {
        super(name, email, password, "RIDER", List.of("RESERVE_DOCK", "UNLOCK_BIKE", "VIEW_STATION"));
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}
