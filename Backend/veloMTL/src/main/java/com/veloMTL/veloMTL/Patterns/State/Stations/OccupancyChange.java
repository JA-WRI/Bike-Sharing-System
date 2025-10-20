package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Station;

public interface OccupancyChange {
    void handleOccupancyChange(Station station);
}
