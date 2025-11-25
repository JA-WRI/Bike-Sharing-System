package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceDockStateTest {
    Dock dock = new Dock();

    @BeforeEach
    void setup() {
        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new MaintenanceDockState());
    }
    LocalDateTime date = LocalDateTime.now();

    @Test
    void reserve_Dock_in_maintenance() {
        StateChangeResponse response = dock.getState().reserveDock(dock, "R001",date);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
        assertEquals("Cannot reserve this dock", response.getMessage());
    }

    @Test
    void mark_maintenance_dock_Dock_Out_Of_Service() {
        StateChangeResponse response = dock.getState().markDockOutOfService(dock);
        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Dock is already out of service", response.getMessage());
    }

    @Test
    void restore_maintenance_dock_to_Service() {
        StateChangeResponse response = dock.getState().restoreService(dock);
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Dock is in service", response.getMessage());

        assertEquals(DockStatus.EMPTY, dock.getStatus());
        assertInstanceOf(EmptyDockState.class, dock.getState());

    }
}