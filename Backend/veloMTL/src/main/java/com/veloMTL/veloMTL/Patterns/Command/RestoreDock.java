package com.veloMTL.veloMTL.Patterns.Command;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.ResponseDTO;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

public class RestoreDock implements Command<ResponseDTO<DockDTO>>{
    private final DockService dockService;
    private final String dockId;
    private final String operatorId;

    public RestoreDock(DockService dockService, String dockId, String operatorId) {
        this.dockService = dockService;
        this.dockId = dockId;
        this.operatorId = operatorId;
    }
    @Override
    public ResponseDTO<DockDTO> execute() {
        return dockService.restoreDockStatus(dockId);
    }
}
