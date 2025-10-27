package com.veloMTL.veloMTL.DTO.Users;

import java.util.List;

public class RiderDTO extends UserDTO {
    private String reservationId;

    private String paymentInfo;

    public RiderDTO() {
        super();
    }

    public RiderDTO(String id, String name, String email, String role, String paymentInfo) {
        super(id, name, email, role);
        this.paymentInfo = paymentInfo;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
