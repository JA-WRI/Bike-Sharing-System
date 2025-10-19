package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.Station;

public class FullStationState implements StationState{


    @Override
    public String markStationOutOfService(Station station) {
        return ("Please empty station before marking it as out og service.");
    }

    @Override
    public String restoreStation(Station station) {
        return ("Station is already in service");
    }
}
