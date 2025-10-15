package com.veloMTL.veloMTL.Model;

import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Docks")
public class Dock {

    @Id
    private String id;
    private DockStatus status;

    @DBRef(lazy = true)
    private Station station;

    public Dock(){};

    public Dock(Station station) {
        this.status = DockStatus.EMPTY;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
