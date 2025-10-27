package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;

import java.time.LocalDateTime;

public class RiderReserveBike implements Command<ResponseDTO<BikeDTO>>{
    private BikeService bikeService;
    private LocalDateTime reserveDate;
    private String bikeId;
    private String username;
    private String dockId;

    public RiderReserveBike(BikeService bikeService, String dockId, LocalDateTime date, String bikeId,
                            String username) {
        this.bikeService = bikeService;
        this.reserveDate = date;
        this.bikeId = bikeId;
        this.username = username;
        this.dockId = dockId;
    }

    public ResponseDTO<BikeDTO> execute() {
        return bikeService.reserveBike(bikeId, username, dockId, reserveDate);
    }

}
