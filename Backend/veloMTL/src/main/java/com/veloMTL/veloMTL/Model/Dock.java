package com.veloMTL.veloMTL.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Docks")
public class Dock {

    @Id
    private String id;
    private String status;

    @DBRef(lazy = true)
    private Station station;

    public Dock(){};

    public Dock(String status, Station station) {
        this.status = status;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
