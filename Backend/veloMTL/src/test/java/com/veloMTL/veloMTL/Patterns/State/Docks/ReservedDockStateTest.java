package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservedDockStateTest {
    Dock dock = new Dock();

    @BeforeEach
    void setup() {
        dock.setStatus(DockStatus.RESERVED);
        dock.setState(new ReservedDockState());
    }
    LocalDateTime date = LocalDateTime.now();

    @Test
    void reserveDock() {
        StateChangeResponse response = dock.getState().reserveDock(dock, "R001",date);
        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Dock already reserved", response.getMessage());
    }

    @Test
    void mark_reserved_Dock_Out_Of_Service() {
        StateChangeResponse response = dock.getState().markDockOutOfService(dock);
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Dock is set to out of service", response.getMessage());

        assertEquals(DockStatus.OUT_OF_SERVICE, dock.getStatus());
        assertInstanceOf(MaintenanceDockState.class,dock.getState());
        assertEquals(null, dock.getReserveUser());
    }

    @Test
    void restoreService() {
    }
}