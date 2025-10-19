package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.Station;

public interface StationState {
    String markStationOutOfService(Station station);
    String restoreStation(Station station);

}
