package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceBikeStateTest {
    Bike bike = new Bike();
    Dock dock = new Dock();
    String userId = "user123";
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setup() {
        this.bike = new Bike();
        this.dock = new Dock();
        this.userId = "user123";

        this.bike.setBikeId("bike123");
        this.bike.setState(new MaintenanceBikeState());
    }

    @Test
    void unlockBikeInMaintenance() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.RIDER, this.now, this.userId);
        assertEquals(StateChangeStatus.INVALID_TRANSITION, response.getStatus());
    }

    @Test
    void lockBikeInMaintenanceRider() {
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
    }

    @Test
    void lockBikeInMaintenanceOperator() {
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.OPERATOR);

        assertEquals(BikeStatus.AVAILABLE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof AvailableBikeState);
        assertEquals(this.dock, this.bike.getDock());

        assertEquals(this.dock.getBike(), this.bike.getBikeId());
        assertEquals(DockStatus.OCCUPIED, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof OccupiedDockState);

        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void reserveBikeInMaintenance() {
        StateChangeResponse response = this.bike.getState().reserveBike(this.bike, this.dock, this.now, this.userId);
        assertEquals(StateChangeStatus.INVALID_TRANSITION, response.getStatus());
    }
}