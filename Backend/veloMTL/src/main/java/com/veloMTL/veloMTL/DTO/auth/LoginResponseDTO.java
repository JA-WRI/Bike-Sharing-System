package com.veloMTL.veloMTL.DTO.auth;

import com.veloMTL.veloMTL.DTO.Helper.LoyaltyTierDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;

public class LoginResponseDTO {
    private String token;
    private String id;
    private String name;
    private String email;
    private UserStatus role;
    private LoyaltyTierDTO tierChange;

    public LoginResponseDTO(String token,String id, String name, String email, UserStatus role) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
        this.id = id;
        this.tierChange = null;
    }

    public LoginResponseDTO(String token,String id, String name, String email, UserStatus role, LoyaltyTierDTO tierChange) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
        this.id = id;
        this.tierChange = tierChange;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public LoyaltyTierDTO getTierChange() {
        return tierChange;
    }

    public void setTierChange(LoyaltyTierDTO tierChange) {
        this.tierChange = tierChange;
    }
}
