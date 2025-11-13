package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "Operators")
public class Operator extends User{

    public Operator() {
        super();
        this.setRole(Set.of(UserStatus.OPERATOR, UserStatus.RIDER));
    }

    public Operator(String name, String email, String password) {
        super(name, email, password, Set.of(UserStatus.OPERATOR, UserStatus.RIDER),
                List.of(Permissions.DOCK_OOS, Permissions.RESTORE_DOCK, Permissions.STATION_OOS, Permissions.RESTORE_STATION, Permissions.BIKE_MOVE));
    }
}
