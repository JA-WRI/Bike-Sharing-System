package com.veloMTL.veloMTL.DTO;

import com.veloMTL.veloMTL.Model.Rider;

public class RiderDTO {

    private String name;
    private String email;

    public RiderDTO() {}

    // Constructor to create DTO from User entity
    public RiderDTO(Rider rider) {
        this.name = rider.getName();
        this.email = rider.getEmail();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
