package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.MaintenanceDockState;
import com.veloMTL.veloMTL.Service.BMSCore.NotificationService;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class Empty_Station_Test {

    @Mock
    NotificationService notificationService;

    Station station;
    Dock dock1;
    Dock dock2;


    @Test
    void mark_station_out_of_service_when_station_empty_test(){
        StateChangeResponse response = station.getStationState().markStationOutOfService(station);
        for (Dock dock : station.getDocks()) {
            assertEquals(DockStatus.OUT_OF_SERVICE, dock.getStatus());
            assertInstanceOf(MaintenanceDockState.class, dock.getState());
        }
        assertEquals(StationStatus.OUT_OF_SERVICE, station.getStationStatus());
        assertInstanceOf(MaintenanceStationState.class, station.getStationState());

        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Station was marked out of service", response.getMessage());
    }
    @Test
    void restore_station_when_station_empty_test(){
        StateChangeResponse response = station.getStationState().restoreStation(station);
        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Station is already in service", response.getMessage());
    }
    @BeforeEach
    void setup(){
        station = new Station();
        dock1 = new Dock();
        dock2 = new Dock();
        dock1.setStation(station);
        dock1.setStation(station);
        station.setDocks(List.of(dock1, dock2));

        station.setStationState(new EmptyStationState(notificationService));
    }


}
