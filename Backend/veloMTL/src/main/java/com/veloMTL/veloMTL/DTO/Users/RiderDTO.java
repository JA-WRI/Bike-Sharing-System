package com.veloMTL.veloMTL.DTO.Users;

import java.util.List;

public class RiderDTO extends UserDTO {
    private String reservationId;

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
}
