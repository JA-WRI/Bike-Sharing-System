package com.veloMTL.veloMTL.DTO.Users;

import com.veloMTL.veloMTL.Model.Enums.UserStatus;

public class OperatorDTO extends UserDTO {
    public OperatorDTO(){
        super();
    }

    public OperatorDTO(String id, String name, String email, UserStatus role) {
        super(id, name, email, role);
    }

}

