package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FullStationTest {
    Station station;

    @Test
    void mark_station_out_of_service_when_station_full_test(){
        station = new Station();
        station.setStationState(new FullStationState());
        StateChangeResponse response = station.getStationState().markStationOutOfService(station);

        assertEquals(StateChangeStatus.INVALID_TRANSITION, response.getStatus());
        assertEquals("Please empty station before marking it as out og service.", response.getMessage());

    }
    @Test
    void restore_station_when_station_full_test(){
        station = new Station();
        station.setStationState(new FullStationState());
        StateChangeResponse response = station.getStationState().restoreStation(station);

        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Station is already in service", response.getMessage());
    }
}
