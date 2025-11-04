package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

import java.time.LocalDateTime;

public class ReserveDock implements Command<ResponseDTO<DockDTO>> {
    private final DockService dockService;
    private final String riderId;
    private final String dockId;
    private final LocalDateTime reservationTime;
    private final UserStatus role;

    public ReserveDock(DockService dockService, String riderId, String dockId, LocalDateTime reservationTime, UserStatus role) {
        this.dockService = dockService;
        this.riderId = riderId;
        this.dockId = dockId;
        this.reservationTime = reservationTime;
        this.role = role;
    }

    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.reserveDock(dockId, riderId, reservationTime, role);

    }
}
