package com.veloMTL.veloMTL.DTO.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;

import java.util.List;

public class StationDTO {

    private String id;
    private String stationName;
    private String position;
    private String streetAddress;
    private StationStatus stationStatus;
    private int capacity;
    private int occupancy;
    private List<DockDTO> docks;

    public StationDTO(){};

    public StationDTO(List<DockDTO> docks, int capacity, StationStatus stationStatus, String streetAddress, String position, String stationName, String id, int occupancy) {
        this.docks = docks;
        this.capacity = capacity;
        this.stationStatus = stationStatus;
        this.streetAddress = streetAddress;
        this.position = position;
        this.stationName = stationName;
        this.id = id;
        this.occupancy = occupancy;
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

    public List<DockDTO> getDocks() {
        return docks;
    }

    public void setDocks(List<DockDTO> docks) {
        this.docks = docks;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }
}
