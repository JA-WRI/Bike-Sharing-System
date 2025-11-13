package com.veloMTL.veloMTL.Model.Users;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

public abstract class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private UserStatus role;
    private List<Permissions> permissions;
    private double flexDollars;
    private String reservationId; //only if a user reserves a bike
    private String stripeCustomerId;
    private Plan plan;


    public User() {
    }

    public User(String name, String email, String password, UserStatus role, List<Permissions> permissions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getRole() {
        return role;
    }

    public void setRole(UserStatus role) {
        this.role = role;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public double getFlexDollars() {
        return flexDollars;
    }

    public void setFlexDollars(double flexDollars) {
        this.flexDollars = flexDollars;
    }
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
