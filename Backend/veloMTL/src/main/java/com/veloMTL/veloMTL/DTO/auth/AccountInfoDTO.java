package com.veloMTL.veloMTL.DTO.auth;

import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;

public class AccountInfoDTO {
    private String id;
    private String name;
    private String email;
    private UserStatus role;
    private double flexDollars;
    private LoyaltyTier tier;

    public AccountInfoDTO() {}

    public AccountInfoDTO(String id, String name, String email, UserStatus role, double flexDollars, LoyaltyTier tier) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.flexDollars = flexDollars;
        this.tier = tier;
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

    public UserStatus getRole() {
        return role;
    }

    public void setRole(UserStatus role) {
        this.role = role;
    }

    public double getFlexDollars() {
        return flexDollars;
    }

    public void setFlexDollars(double flexDollars) {
        this.flexDollars = flexDollars;
    }

    public LoyaltyTier getTier() {
        return tier;
    }

    public void setTier(LoyaltyTier tier) {
        this.tier = tier;
    }
}

