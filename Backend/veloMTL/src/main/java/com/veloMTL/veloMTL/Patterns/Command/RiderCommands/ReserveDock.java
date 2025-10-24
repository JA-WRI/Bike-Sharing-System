package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

import java.time.LocalDateTime;

public class ReserveDock implements Command<ResponseDTO<DockDTO>> {
    private DockService dockService;
    private String riderId;
    private String dockId;
    private LocalDateTime reservationTime;

    public ReserveDock(DockService dockService, String riderId, String dockId, LocalDateTime reservationTime) {
        this.dockService = dockService;
        this.riderId = riderId;
        this.dockId = dockId;
        this.reservationTime = reservationTime;
    }

    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.reserveDock(dockId, riderId, reservationTime);

    }
}
