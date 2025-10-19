package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Station;
import com.veloMTL.veloMTL.Patterns.State.Docks.MaintenanceDockState;

import java.util.List;

public class EmptyStationState implements StationState{

    @Override
    public String markStationOutOfService(Station station) {
        List<Dock> docks = station.getDocks();
        for(Dock dock: docks){
            dock.setStatus(DockStatus.OUT_OF_SERVICE);
            dock.setState(new MaintenanceDockState());
        }
        station.setStationStatus(StationStatus.OUT_OF_SERVICE);
        station.setStationState(new MaintenanceStationState());
        return ("Station was marked out of service");


    }

    @Override
    public String restoreStation(Station station) {
        return ("Station is already in service");
    }
}
