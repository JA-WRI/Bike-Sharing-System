package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "Riders")
public class Rider extends User{


//    private String reservationId; //only if a user reserves a bike
//    private String stripeCustomerId;
//    private Plan plan;
//

    public Rider() {
        super();
        this.setRole("RIDER");
    }

    public Rider(String name, String email, String password) {
        super(name, email, password, "RIDER",
                List.of(Permissions.BIKE_UNLOCK, Permissions.BIKE_RETURN, Permissions.BIKE_RESERVE, Permissions.DOCK_RESERVE));
    }

//    public String getReservationId() {
//        return reservationId;
//    }
//
//    public void setReservationId(String reservationId) {
//        this.reservationId = reservationId;
//    }
//
//    public String getStripeCustomerId() {
//        return stripeCustomerId;
//    }
//
//    public void setStripeCustomerId(String stripeCustomerId) {
//        this.stripeCustomerId = stripeCustomerId;
//    }
//
//    public Plan getPlan() {
//        return plan;
//    }
//
//    public void setPlan(Plan plan) {
//        this.plan = plan;
//    }

}
