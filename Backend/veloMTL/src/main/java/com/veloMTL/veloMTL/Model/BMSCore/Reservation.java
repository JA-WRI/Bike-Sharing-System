package com.veloMTL.veloMTL.Model.BMSCore;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Reservations")
public class Reservation {
    @Id
    private String reservationId;
    private String reservationType;
    private LocalDateTime reserveStart;
    private LocalDateTime reserveEnd;
//    Update the naming later
    private String reserveUser;
    private String reserveBike;
    private String reserveDock;

    public Reservation() {}

    public Reservation(String reserveUser, String reserveBike, String reserveDock) {
        this.reserveUser = reserveUser;
        this.reserveBike = reserveBike;
        this.reserveDock = reserveDock;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public LocalDateTime getReserveStart() {
        return reserveStart;
    }

    public void setReserveStart(LocalDateTime reserveStart) {
        this.reserveStart = reserveStart;
    }

    public LocalDateTime getReserveEnd() {
        return reserveEnd;
    }

    public void setReserveEnd(LocalDateTime reserveEnd) {
        this.reserveEnd = reserveEnd;
    }

    public String getReserveUser() {
        return reserveUser;
    }

    public void setReserveUser(String reserveUser) {
        this.reserveUser = reserveUser;
    }

    public String getReserveBike() {
        return reserveBike;
    }

    public void setReserveBike(String reserveBike) {
        this.reserveBike = reserveBike;
    }

    public String getReserveDock() {
        return reserveDock;
    }

    public void setReserveDock(String reserveDock) {
        this.reserveDock = reserveDock;
    }
}
