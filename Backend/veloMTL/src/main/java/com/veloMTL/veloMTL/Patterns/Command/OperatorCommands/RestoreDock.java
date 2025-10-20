package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

public class RestoreDock implements Command<ResponseDTO<DockDTO>> {
    private final DockService dockService;
    private final String dockId;
    private final String operatorId;

    public RestoreDock(DockService dockService, String operatorId, String dockId) {
        this.dockService = dockService;
        this.dockId = dockId;
        this.operatorId = operatorId;
    }
    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.restoreDockStatus(dockId);
    }
}
