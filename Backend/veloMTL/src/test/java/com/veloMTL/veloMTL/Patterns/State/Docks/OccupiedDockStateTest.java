package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OccupiedDockStateTest {
    Dock dock = new Dock();
    @BeforeEach
    void setup() {
        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new OccupiedDockState());
    }
    LocalDateTime date = LocalDateTime.now();
    @Test
    void reserve_occupied_Dock() {
        StateChangeResponse response = dock.getState().reserveDock(dock,"R001",date);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
        assertEquals("Dock is currently occupied", response.getMessage());
    }

    @Test
    void mark_Occupied_Dock_Out_Of_Service() {
        StateChangeResponse response = dock.getState().markDockOutOfService(dock);
        assertEquals(StateChangeStatus.INVALID_TRANSITION, response.getStatus());
        assertEquals("Bike needs to be removed first", response.getMessage());
    }

    @Test
    void restoreService() {
        StateChangeResponse response = dock.getState().restoreService(dock);
        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
        assertEquals("Dock is already in service", response.getMessage());
    }
}