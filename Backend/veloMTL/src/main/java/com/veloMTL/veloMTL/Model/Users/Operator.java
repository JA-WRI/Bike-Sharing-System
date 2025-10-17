package com.veloMTL.veloMTL.Model.Users;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Operators")
public class Operator extends User{

    public Operator() {
        super();
        this.setRole("OPERATOR");
    }

    public Operator(String name, String email, String password) {
        super(name, email, password, "OPERATOR", List.of("MANAGE_STATIONS", "MANAGE-DOCKS", "MANAGE_BIKES"));
    }
}
