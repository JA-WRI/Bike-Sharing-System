package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Operators")
public class Operator extends User{

    public Operator() {
        super();
        this.setRole("OPERATOR");
    }

    public Operator(String name, String email, String password) {
        super(name, email, password, "OPERATOR",
                List.of(Permissions.DOCK_OOS, Permissions.RESTORE_DOCK, Permissions.STATION_OOS, Permissions.RESTORE_STATION, Permissions.BIKE_MOVE));
    }
}
