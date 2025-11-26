package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptyDockStateTest {

Dock dock = new Dock();

    @BeforeEach
    void setup() {
        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new EmptyDockState());
    }
LocalDateTime date = LocalDateTime.now();
    @Test
    void return_success_confirmation_when_reserving_an_empty_dock() {
        StateChangeResponse response = dock.getState().reserveDock(dock, "R001", date);
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Dock has been reserved", response.getMessage());

        assertEquals(DockStatus.RESERVED, dock.getStatus());
        assertEquals("R001", dock.getReserveUser());
        assertEquals(date, dock.getReserveDate());
        assertInstanceOf(ReservedDockState.class, dock.getState());
    }

    @Test
    void mark_empty_Dock_Out_Of_Service() {
        StateChangeResponse response = dock.getState().markDockOutOfService(dock);
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Dock is set to out of service", response.getMessage());

        assertEquals(DockStatus.OUT_OF_SERVICE, dock.getStatus());
        assertInstanceOf(MaintenanceDockState.class, dock.getState());
    }

    @Test
    void restore_empty_dock_Service() {
        StateChangeResponse response = dock.getState().restoreService(dock);
        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Dock is already in service", response.getMessage());
    }
}