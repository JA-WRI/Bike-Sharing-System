package com.veloMTL.veloMTL.Patterns.State.Bikes;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

class OnTripBikeStateTest {
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
        this.bike.setState(new OnTripBikeState());
        this.bike.setBikeStatus(BikeStatus.ON_TRIP);
    }

    @Test
    void unlockBikeOnTrip() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.RIDER, this.now, this.userId);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
    }

    @Test
    void lockBikeOnTripToEmptyDock() {
        this.dock.setStatus(DockStatus.EMPTY);
        this.dock.setState(new EmptyDockState());
        
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        
        assertEquals(BikeStatus.AVAILABLE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof AvailableBikeState);
        assertNull(this.bike.getReserveUser());
        assertNull(this.bike.getReserveDate());
        
        assertEquals(DockStatus.OCCUPIED, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof OccupiedDockState);
        assertNull(this.dock.getReserveDate());
        assertNull(this.dock.getReserveUser());

        assertEquals(this.dock, this.bike.getDock());
        assertEquals(this.bike.getBikeId(), this.dock.getBike());

        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void lockBikeOnTripToReservedDockExpired() {
        this.dock.setStatus(DockStatus.RESERVED);
        this.dock.setReserveDate(LocalDateTime.now().minusMinutes(20));
        this.dock.setReserveUser(this.userId);
        
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        
        assertEquals(DockStatus.EMPTY, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof EmptyDockState);
        assertNull(this.dock.getReserveDate());
        assertNull(this.dock.getReserveUser());
        
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
    }

    @Test
    void lockBikeOnTripToReservedDockValid() {
        this.dock.setStatus(DockStatus.RESERVED);
        this.dock.setReserveDate(LocalDateTime.now().plusMinutes(10));
        
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        
        assertEquals(BikeStatus.AVAILABLE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof AvailableBikeState);
        assertNull(this.bike.getReserveDate());
        assertNull(this.bike.getReserveUser());

        assertEquals(DockStatus.OCCUPIED, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof OccupiedDockState);
        assertNull(this.dock.getReserveDate());
        assertNull(this.dock.getReserveUser());

        assertEquals(this.dock, this.bike.getDock());
        assertEquals(this.bike.getBikeId(), this.dock.getBike());
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void lockBikeOnTripToOccupiedDock() {
        this.dock.setStatus(DockStatus.OCCUPIED);
        
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
    }

    @Test
    void reserveBikeOnTrip() {
        StateChangeResponse response = this.bike.getState().reserveBike(this.bike, this.dock, this.now, this.userId);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
    }
}