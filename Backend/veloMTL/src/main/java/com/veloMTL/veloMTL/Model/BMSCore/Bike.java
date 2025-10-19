package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Bikes")
@TypeAlias("bike")
public class Bike {
    @Id
    private String bikeId;
    private String bikeType;
    private BikeStatus bikeStatus;

    @DBRef(lazy = true)
    private Dock dock;

    public Bike(String bikeId, String bikeType, BikeStatus bikeStatus, Dock dock) {
        this.bikeType = bikeType;
        this.bikeStatus = bikeStatus;
        this.dock = dock;
        this.bikeId = bikeId;
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
}
