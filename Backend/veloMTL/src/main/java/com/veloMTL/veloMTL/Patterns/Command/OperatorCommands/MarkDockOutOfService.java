package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

public class MarkDockOutOfService implements Command<ResponseDTO<DockDTO>> {
    private final DockService dockService;
    private String operatorId;
    private final String dockId;

    public MarkDockOutOfService(DockService dockService, String operatorId, String dockId){
        this.dockId = dockId;
        this.dockService =dockService;
        this.operatorId = operatorId;
    }

    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.markDockOutOfService(dockId);
    }
}
