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
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

class ReservedBikeStateTest {
    Bike bike = new Bike();
    Dock dock = new Dock();
    String userId = "user123";
    String otherUserId = "user456";
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setup() {
        this.bike = new Bike();
        this.dock = new Dock();
        this.userId = "user123";

        this.bike.setBikeId("bike123");
        this.bike.setState(new ReservedBikeState());
        this.bike.setBikeStatus(BikeStatus.RESERVED);
        this.bike.setReserveUser(this.userId);
        this.bike.setReserveDate(this.now);
    }

    @Test
    void unlockReservedBikeAsOperator() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.OPERATOR, this.now, this.userId);
        
        assertEquals(BikeStatus.OUT_OF_SERVICE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof MaintenanceBikeState);
        
        assertEquals(DockStatus.EMPTY, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof EmptyDockState);
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void unlockReservedBikeAsRiderExpired() {
        this.bike.setReserveDate(this.now.minusMinutes(BikeService.EXPIRY_TIME_MINS + 1));
        
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.RIDER, this.now, this.userId);
        
        assertEquals(BikeStatus.AVAILABLE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof AvailableBikeState);
        assertNull(this.bike.getReserveUser());
        assertNull(this.bike.getReserveDate());
        
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
    }

    @Test
    void unlockReservedBikeAsWrongRider() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.RIDER, this.now, this.otherUserId);
        
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
    }

    @Test
    void unlockReservedBikeAsCorrectRider() {
        StateChangeResponse response = this.bike.getState().unlockBike(this.bike, this.dock, UserStatus.RIDER, this.now, this.userId);
        
        assertEquals(BikeStatus.ON_TRIP, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof OnTripBikeState);
        
        assertEquals(DockStatus.EMPTY, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof EmptyDockState);
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void lockReservedBike() {
        StateChangeResponse response = this.bike.getState().lockBike(this.bike, this.dock, UserStatus.RIDER);
        
        assertEquals(BikeStatus.AVAILABLE, this.bike.getBikeStatus());
        assertTrue(() -> this.bike.getState() instanceof AvailableBikeState);
        assertNull(this.bike.getReserveUser());
        assertNull(this.bike.getReserveDate());
        
        assertEquals(DockStatus.OCCUPIED, this.dock.getStatus());
        assertTrue(() -> this.dock.getState() instanceof OccupiedDockState);
        
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
    }

    @Test
    void reserveAlreadyReservedBike() {
        StateChangeResponse response = this.bike.getState().reserveBike(this.bike, this.dock, this.now, this.otherUserId);
        assertEquals(StateChangeStatus.NOT_ALLOWED, response.getStatus());
    }
}