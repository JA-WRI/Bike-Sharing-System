package com.veloMTL.veloMTL.Model;

import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Stations")
public class Station {

    @Id
    private String id;
    private String stationName;
    private String position;
    private String streetAddress;
    private StationStatus stationStatus;
    private int capacity;

    @DBRef(lazy = true) //will only load the docks when needed
    private List<Dock> docks;

    public Station(){};

    public Station(List<Dock> docks, int capacity, String streetAddress, String position, String stationName) {
        this.docks = docks;
        this.capacity = capacity;
        this.stationStatus = StationStatus.EMPTY;
        this.streetAddress = streetAddress;
        this.position = position;
        this.stationName = stationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StationStatus getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(StationStatus stationStatus) {
        this.stationStatus = stationStatus;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Dock> getDocks() {
        if(docks ==null){
            this.setDocks(new ArrayList<>());
        }
        return docks;
    }

    public void setDocks(List<Dock> docks) {
        this.docks = docks;
    }
}
