package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.Station;

public class OccupiedStationState implements StationState{


    @Override
    public String markStationOutOfService(Station station) {
        return ("Station is already out of service");
    }

    @Override
    public String restoreStation(Station station) {
        return ("Station is already in service");
    }
}
