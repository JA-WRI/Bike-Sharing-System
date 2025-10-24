package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;

public class OperatorMapper {
    public static Operator dtoToEntity(OperatorDTO dto, String password){
        Operator operator = new Operator(
                dto.getName(),
                dto.getEmail(),
                password
        );

        operator.setId(dto.getId());

        return operator;
    }
    public static OperatorDTO entityToDto(Operator entity){
        return new OperatorDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole()
        );
    }
}
