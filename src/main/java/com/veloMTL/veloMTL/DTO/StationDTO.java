package com.veloMTL.veloMTL.DTO;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Model.Dock;
import java.util.List;

public class StationDTO {

    private String id;
    private String stationName;
    private String position;
    private String streetAddress;
    private StationStatus stationStatus;
    private int capacity;
    private List<DockDTO> docks;

    public StationDTO(){};

    public StationDTO(List<DockDTO> docks, int capacity, StationStatus stationStatus, String streetAddress, String position, String stationName, String id) {
        this.docks = docks;
        this.capacity = capacity;
        this.stationStatus = stationStatus;
        this.streetAddress = streetAddress;
        this.position = position;
        this.stationName = stationName;
        this.id = id;
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
}
