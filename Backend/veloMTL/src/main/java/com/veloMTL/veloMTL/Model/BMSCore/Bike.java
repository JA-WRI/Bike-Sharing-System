package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Patterns.State.Bikes.BikeState;
import com.veloMTL.veloMTL.Patterns.State.Docks.DockState;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Bikes")
@TypeAlias("Standard")
public class Bike {
    @Id
    private String bikeId;
    private String bikeType;
    private BikeStatus bikeStatus;
    private LocalDateTime reserveDate;
    private String reserveUser;

    @DBRef(lazy = true)
    private Dock dock;

    @Transient
    private BikeState state;

    public Bike() {}

    public Bike(String bikeId, String bikeType, BikeStatus bikeStatus, Dock dock) {
        this.bikeType = bikeType;
        this.bikeStatus = bikeStatus;
        this.dock = dock;
        this.bikeId = bikeId;
    }

    public Bike(String bikeId, String bikeType, BikeStatus bikeStatus, Dock dock, LocalDateTime reserveDate,
                String reserveUser) {
        this.bikeType = bikeType;
        this.bikeStatus = bikeStatus;
        this.dock = dock;
        this.bikeId = bikeId;
        this.reserveDate = reserveDate;
        this.reserveUser = reserveUser;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public BikeStatus getBikeStatus() {
        return bikeStatus;
    }

    public void setBikeStatus(BikeStatus bikeStatus) {
        this.bikeStatus = bikeStatus;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public BikeState getState() {return state;}

    public void setState(BikeState state) {
        this.state = state;
    }

    public LocalDateTime getReserveDate() { return reserveDate; }

    public String getReserveUser() { return reserveUser; }

    public void setReserveDate(LocalDateTime reserveDate) {
        this.reserveDate = reserveDate;
    }

    public void setReserveUser(String reserveUser) {
        this.reserveUser = reserveUser;
    }
}
