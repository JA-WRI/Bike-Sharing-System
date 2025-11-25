package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Riders")
public class Rider extends User {

    // Optionally keep a field for flexDollars if you need to handle it specifically for Rider.
    // This might not be necessary if flexDollars is inherited from the User class.
    @Override
    @Field("flex_dollars")  // Explicitly specify the field name to avoid ambiguity
    public double getFlexDollars() {
        return super.getFlexDollars();
    }

    @Override
    @Field("flex_dollars")  // Explicitly specify the field name to avoid ambiguity
    public void setFlexDollars(double flexDollars) {
        super.setFlexDollars(flexDollars);
    }

    public Rider() {
        super();
        this.setRole(UserStatus.RIDER);
        this.setFlexDollars(0);  // Initialize Flex Dollars to 0 by default
    }

    public Rider(String name, String email, String password) {
        super(name, email, password, UserStatus.RIDER,
                List.of(Permissions.BIKE_UNLOCK, Permissions.BIKE_RETURN, Permissions.BIKE_RESERVE, Permissions.DOCK_RESERVE));
        this.setFlexDollars(0);  // Initialize Flex Dollars to 0 for new rider
    }
}
