package com.veloMTL.veloMTL.DTO.Users;

import java.util.List;

public class RiderDTO extends UserDTO {
    private String reservationId;

    private String paymentMethod;

    public RiderDTO() {
        super();
    }

    public RiderDTO(String id, String name, String email, String role) {
        super(id, name, email, role);

    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentInfo) {
        this.paymentMethod = paymentInfo;
    }
}
