package com.veloMTL.veloMTL.Model.Users;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Riders")
public class Rider extends User{

    private String paymentInfo; //this should be turned into a PaymentInfo object
    private String reservationId; //only if a user reserves a bike

    public Rider() {
        super();
        this.setRole("RIDER");
    }

    public Rider(String name, String email) {
        super(name, email, null, "RIDER");
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
