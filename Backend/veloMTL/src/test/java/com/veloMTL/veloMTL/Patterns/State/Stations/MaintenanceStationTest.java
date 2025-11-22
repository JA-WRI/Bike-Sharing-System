package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Service.BMSCore.NotificationService;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class MaintenanceStationTest {
    @Mock
    NotificationService notificationService;

    Station station;
    Dock dock1;
    Dock dock2;


    @Test
    void mark_station_out_of_service_when_station_maintenance_test(){
        StateChangeResponse response = station.getStationState().markStationOutOfService(station);

        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Station is already out of service", response.getMessage());
    }
    @Test
    void restore_station_when_station_maintenance_test(){
        StateChangeResponse response = station.getStationState().restoreStation(station);

        for (Dock dock : station.getDocks()) {
            assertEquals(DockStatus.EMPTY, dock.getStatus());
            assertInstanceOf(EmptyDockState.class, dock.getState());
        }
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Station status is restored", response.getMessage());

        assertEquals(StationStatus.EMPTY, station.getStationStatus());
        assertInstanceOf(EmptyStationState.class, station.getStationState());
    }

    @BeforeEach
    void setup(){
        station = new Station();
        dock1 = new Dock();
        dock2 = new Dock();
        dock1.setStation(station);
        dock1.setStation(station);
        station.setDocks(List.of(dock1, dock2));

        station.setStationState(new MaintenanceStationState(notificationService));
    }

}