package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;

import java.util.List;

public class MaintenanceStationState implements StationState{

    private StationRepository stationRepository;

    @Override
    public String markStationOutOfService(Station station) {
        return ("Station is already out of service");
    }

    @Override
    public String restoreStation(Station station) {
        List<Dock> docks = station.getDocks();
        for (Dock dock: docks){
            dock.setStatus(DockStatus.EMPTY);
            dock.setState(new EmptyDockState());

        }
        station.setStationStatus(StationStatus.EMPTY);
        station.setStationState(new EmptyStationState());
        return ("Station status is restored");
    }
}
