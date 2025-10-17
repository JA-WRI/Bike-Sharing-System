package com.veloMTL.veloMTL.Patterns.Command;

import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;

public class DockMaintenanceCommand implements Command{
    private final DockService dockService;
    private String operatorId;
    private final String dockId;

    public DockMaintenanceCommand(DockService dockService, String operatorId, String dockId){
        this.dockId = dockId;
        this.dockService =dockService;
        this.operatorId = operatorId;
    }

    @Override
    public void execute() {
        dockService.updateDockStatus(dockId, DockStatus.OUT_OF_SERVICE);
    }
}
