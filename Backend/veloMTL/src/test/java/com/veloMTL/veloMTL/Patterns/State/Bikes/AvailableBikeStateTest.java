package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AvailableBikeStateTest {
    Bike bike;
    Dock dock;
    String userId;
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setup() {
        this.bike = new Bike();
        this.dock = new Dock();
        this.userId = "user123";

        this.bike.setBikeId("bike123");
        this.bike.setState(new AvailableBikeState());
    }

    @Test
    void unlockBikeAsRider() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.RIDER, this.now, this.userId);
        
        assertEquals(BikeStatus.ON_TRIP, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof OnTripBikeState);
        assertEquals(DockStatus.EMPTY, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof EmptyDockState);
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void unlockBikeAsOperator() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.OPERATOR, this.now, this.userId);
        
        assertEquals(BikeStatus.OUT_OF_SERVICE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof MaintenanceBikeState);
        assertEquals(DockStatus.EMPTY, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof EmptyDockState);
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void unlockBikeAsGuest() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.GUEST, this.now, this.userId);
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
    }

    @Test
    void lockBikeAvailable() {
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        assertEquals(StateChangeStatus.ALREADY_IN_DESIRED_STATE, response.getStatus());
    }

    @Test
    void reserveBikeAvailable() {
        StateChangeResponse response = this.bike.getState().reserveBike(this.bike, this.dock, this.now, this.userId);
        
        assertEquals(BikeStatus.RESERVED, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof ReservedBikeState);
        assertEquals(this.now, this.bike.getReserveDate());
        assertEquals(this.userId, this.bike.getReserveUser());
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }
}
