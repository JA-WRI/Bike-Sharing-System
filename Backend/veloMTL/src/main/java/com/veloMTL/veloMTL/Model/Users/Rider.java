package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import java.util.Set;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "Riders")
public class Rider extends User{

    public Rider() {
        super();
        this.setRole(Set.of(UserStatus.RIDER));
    }

    public Rider(String name, String email, String password) {
        super(name, email, password, Set.of(UserStatus.RIDER),
                List.of(Permissions.BIKE_UNLOCK, Permissions.BIKE_RETURN, Permissions.BIKE_RESERVE, Permissions.DOCK_RESERVE));
    }

}
