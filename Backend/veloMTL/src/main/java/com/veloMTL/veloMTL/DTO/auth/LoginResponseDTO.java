package com.veloMTL.veloMTL.DTO.auth;

import com.veloMTL.veloMTL.Model.Enums.UserStatus;

public class LoginResponseDTO {
    private String token;
    private String id;
    private String name;
    private String email;
    private UserStatus role;
    private double flexDollars;  // Add Flex Dollars field

    // Constructor including Flex Dollars
    public LoginResponseDTO(String token, String id, String name, String email, UserStatus role, double flexDollars) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.flexDollars = flexDollars;  // Initialize Flex Dollars
    }

    // Getters and Setters for all fields
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for Flex Dollars
    public double getFlexDollars() {
        return flexDollars;
    }

    public void setFlexDollars(double flexDollars) {
        this.flexDollars = flexDollars;
    }
}
