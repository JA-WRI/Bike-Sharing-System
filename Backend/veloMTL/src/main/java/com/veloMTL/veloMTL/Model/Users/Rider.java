package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Patterns.Strategy.PaymentMethod;
import com.veloMTL.veloMTL.Model.Enums.Permissions;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "Riders")
public class Rider extends User{

    private PaymentMethod paymentMethod; //this should be turned into a PaymentMethod object
    private String reservationId; //only if a user reserves a bike
    private String stripeCustomerId;


    public Rider() {
        super();
        this.setRole("RIDER");
    }

    public Rider(String name, String email, String password) {
        super(name, email, password, "RIDER",
                List.of(Permissions.BIKE_UNLOCK, Permissions.BIKE_RETURN, Permissions.BIKE_RESERVE, Permissions.DOCK_RESERVE));
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }


}
