package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

public class RestoreDock implements Command<ResponseDTO<DockDTO>> {
    private final DockService dockService;
    private final String dockId;
    private final String operatorId;
    private final UserStatus role;

    public RestoreDock(DockService dockService, String operatorId, String dockId, UserStatus role) {
        this.dockService = dockService;
        this.dockId = dockId;
        this.operatorId = operatorId;
        this.role = role;
    }
    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.restoreDockStatus(dockId, role);
    }
}
