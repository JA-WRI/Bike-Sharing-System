package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

public class MarkDockOutOfService implements Command<ResponseDTO<DockDTO>> {
    private final DockService dockService;
    private final String userId;
    private final String dockId;
    private final UserStatus role;

    public MarkDockOutOfService(DockService dockService, String operatorId, String dockId, UserStatus role){
        this.dockId = dockId;
        this.dockService =dockService;
        this.userId = operatorId;
        this.role = role;
    }

    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.markDockOutOfService(dockId, role);
    }
}
