package com.veloMTL.veloMTL.DTO.Users;

import com.veloMTL.veloMTL.Model.Enums.UserStatus;

import java.util.List;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private UserStatus role;

    public UserDTO() {
    }

    public UserDTO(String id, String name, String email, UserStatus role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
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

}

