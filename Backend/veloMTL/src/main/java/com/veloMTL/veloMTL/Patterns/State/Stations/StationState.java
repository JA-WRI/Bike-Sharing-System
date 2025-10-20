package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public interface StationState {
    StateChangeResponse markStationOutOfService(Station station);
    StateChangeResponse restoreStation(Station station);

}
