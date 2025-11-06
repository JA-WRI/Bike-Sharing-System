package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;

import java.time.LocalDateTime;

public class RiderReserveBike implements Command<ResponseDTO<BikeDTO>>{
    private final BikeService bikeService;
    private final LocalDateTime reserveDate;
    private final String bikeId;
    private final String username;
    private final String dockId;
    private final UserStatus role;

    public RiderReserveBike(BikeService bikeService, String dockId, LocalDateTime date, String bikeId,
                            String username, UserStatus role) {
        this.bikeService = bikeService;
        this.reserveDate = date;
        this.bikeId = bikeId;
        this.username = username;
        this.dockId = dockId;
        this.role = role;
    }

    public ResponseDTO<BikeDTO> execute() {
        return bikeService.reserveBike(bikeId, username, dockId, reserveDate, role);
    }

}
