package com.veloMTL.veloMTL.DTO;

public class RegistrationDTO {

    private String name;
    private String email;
    private String password;


    public RegistrationDTO(String name, String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
