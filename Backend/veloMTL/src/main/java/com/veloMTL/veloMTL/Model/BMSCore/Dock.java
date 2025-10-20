package com.veloMTL.veloMTL.Model.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.DockState;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Docks")
public class Dock {

    @Id
    private String dockId;
    private DockStatus status;

    @DBRef(lazy = true)
    private Station station;

    @DBRef(lazy = true)
    private Bike bike;

    @Transient
    private DockState state;

    public Dock(){};

    public Dock(String dockId, Station station) {
        this.dockId = dockId;
        this.status = DockStatus.EMPTY;
        this.station = station;
    }

    public String getDockId() {
        return dockId;
    }

    public void setDockId(String dockId) {
        this.dockId = dockId;
    }

    public DockStatus getStatus() {
        return status;
    }

    public void setStatus(DockStatus status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public DockState getState() {
        return state;
    }

    public void setState(DockState state) {
        this.state = state;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

}
