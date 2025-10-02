package com.veloMTL.veloMTL.Model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Riders")
public class Rider {

    private String id;
    private String name;
    private String email;

    public Rider(String name, String email) {
        this.name = name;
        this.email = email;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
