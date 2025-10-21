package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;

public class OperatorMapper {
<<<<<<< HEAD
    public static Operator dtoToEntity(OperatorDTO dto, String password){
        Operator operator = new Operator(
                dto.getName(),
                dto.getEmail(),
                password
        );

=======
    public static Operator dtoToEntity(OperatorDTO dto){
        Operator operator = new Operator(
                dto.getName(),
                dto.getEmail(),
                null
        );
>>>>>>> uroosa2
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
