package com.veloMTL.veloMTL.untils.Mappers;

import com.veloMTL.veloMTL.DTO.Users.RiderDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;

public class RiderMapper {
    public static Rider dtoToEntity(RiderDTO dto, String encodedPassword){
        Rider rider = new Rider(
                dto.getName(),
                dto.getEmail(),
                encodedPassword

        );
        rider.setId(dto.getId());
        rider.setReservationId(dto.getReservationId());

        return rider;
    }

    public static RiderDTO entityToDto(Rider entity){
        RiderDTO dto = new RiderDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getPaymentInfo()
        );

        dto.setReservationId(entity.getReservationId());

        return dto;
    }
}
