package com.veloMTL.veloMTL.DTO.Users;

import com.veloMTL.veloMTL.Model.Enums.UserStatus;

public class RiderDTO extends UserDTO {
    private String reservationId;
    private String paymentMethod; // optional: card type like "Visa"
    private String clientSecret;  // new: used for Stripe setup
    private String stripeCustomerId; // optional: if frontend needs it

    public RiderDTO() {
        super();
    }

    public RiderDTO(String id, String name, String email, UserStatus role) {
        super(id, name, email, role);
    }

    // getters and setters
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    public String getStripeCustomerId() { return stripeCustomerId; }
    public void setStripeCustomerId(String stripeCustomerId) { this.stripeCustomerId = stripeCustomerId; }
}
