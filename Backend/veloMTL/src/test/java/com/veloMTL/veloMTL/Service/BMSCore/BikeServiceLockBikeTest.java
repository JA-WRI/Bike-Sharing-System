package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Patterns.State.Bikes.BikeState;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.Service.PRC.BillingService;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BikeServiceLockBikeTest {
    @Mock
    BikeRepository bikeRepository;
    @Mock
    DockRepository dockRepository;
    @Mock
    StationRepository stationRepository;
    @Mock
    StationService stationService;
    @Mock
    TripService tripService;
    @Mock
    BikeState bikeState;
    @Mock
    RiderRepository riderRepository;
    @Mock
    BillingService billingService;
    @Mock
    TimerService timerService;

    @InjectMocks
    BikeService bikeService;

    Station station;
    Dock dock;
    Bike bike;
    BikeService spyService;

    @Test
    void lock_bike_success_forRider(){
        String userId = "user1";
        mockStateResponse(UserStatus.RIDER, StateChangeStatus.SUCCESS, "Bike has been locked");
        when(bikeRepository.save(any())).thenReturn(bike);

        Trip trip = new Trip();
        when(tripService.findOngoingTrip(bike.getBikeId(), userId)).thenReturn(trip);

        Billing billing = new Billing();
        when(billingService.pay(trip)).thenReturn(billing);

        //act
        ResponseDTO<BikeDTO> response = spyService.lockBike(bike.getBikeId(),userId, dock.getDockId(), UserStatus.RIDER);

        //assert
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Bike has been locked", response.getMessage());
        assertNotNull(response.getData());

        // Check linking
        assertEquals(dock, bike.getDock());
        assertEquals(bike.getBikeId(), dock.getBike());

        // Verify occupancy increased
        verify(stationService).updateStationOccupancy(eq(station), eq(station.getOccupancy() + 1));

        // Verify saves
        verify(bikeRepository).save(bike);
        verify(dockRepository).save(dock);
        verify(stationRepository).save(station);

        // Verify trip logic
        verify(tripService).findOngoingTrip(bike.getBikeId(), userId);
        verify(billingService).pay(trip);
        verify(tripService).endTrip(eq(trip), eq(station), eq(billing));


    }

    @Test
    void lock_bike_invalid_forRider(){
        String userId = "user1";
        mockStateResponse(UserStatus.RIDER, StateChangeStatus.FAILURE, "Cannot lock bike");

        //act
        ResponseDTO<BikeDTO> response = spyService.lockBike(bike.getBikeId(),userId, dock.getDockId(), UserStatus.RIDER);

        //assert
        assertEquals(station, dock.getStation());
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("Cannot lock bike", response.getMessage());
        assertNotNull(response.getData());

        //verify nothing was set
        assertNull(dock.getBike());
        assertNull(bike.getDock());

        //verify station occupancy was not updated
        verify(stationService, never()).updateStationOccupancy(eq(station), anyInt());

        //verify nothing was saved to the database
        verify(bikeRepository, never()).save(bike);
        verify(dockRepository, never()).save(dock);
        verify(stationRepository, never()).save(station);

        //verify no trip was started and no billing was created
        verify(tripService, never()).findOngoingTrip(bike.getBikeId(), userId);
        verify(billingService, never()).pay(any());
        verify(tripService, never()).endTrip(any(), eq(station), any());
    }


    @Test
    void lock_bike_success_forOperator(){
        String userId = "user1";
        mockStateResponse(UserStatus.OPERATOR, StateChangeStatus.SUCCESS, "Bike is locked and back in service");
        when(bikeRepository.save(any())).thenReturn(bike);
        //act
        ResponseDTO<BikeDTO> response = spyService.lockBike(bike.getBikeId(),userId, dock.getDockId(), UserStatus.OPERATOR);
        //assert
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Bike is locked and back in service", response.getMessage());
        assertNotNull(response.getData());

        // Check linking
        assertEquals(dock, bike.getDock());
        assertEquals(bike.getBikeId(), dock.getBike());

        // Verify occupancy increased
        verify(stationService).updateStationOccupancy(eq(station), eq(station.getOccupancy() + 1));

        // Verify saves
        verify(bikeRepository).save(bike);
        verify(dockRepository).save(dock);
        verify(stationRepository).save(station);

        //verify no trip was started and no billing was created
        verify(tripService, never()).findOngoingTrip(bike.getBikeId(), userId);
        verify(billingService, never()).pay(any());
        verify(tripService, never()).endTrip(any(), eq(station), any());
    }

    @Test
    void lock_bike_invalid_forOperator(){
        String userId = "user1";
        mockStateResponse(UserStatus.OPERATOR, StateChangeStatus.FAILURE, "Cannot lock bike");

        //act
        ResponseDTO<BikeDTO> response = spyService.lockBike(bike.getBikeId(),userId, dock.getDockId(), UserStatus.OPERATOR);

        //assert
        assertEquals(station, dock.getStation());
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("Cannot lock bike", response.getMessage());
        assertNotNull(response.getData());

        //verify nothing was set
        assertNull(dock.getBike());
        assertNull(bike.getDock());

        //verify station occupancy was not updated
        verify(stationService, never()).updateStationOccupancy(eq(station), anyInt());

        //verify nothing was saved to the database
        verify(bikeRepository, never()).save(bike);
        verify(dockRepository, never()).save(dock);
        verify(stationRepository, never()).save(station);

        //verify no trip was started and no billing was created
        verify(tripService, never()).findOngoingTrip(bike.getBikeId(), userId);
        verify(billingService, never()).pay(any());
        verify(tripService, never()).endTrip(any(), eq(station), any());

    }

    @BeforeEach
    void setupMocksForLockingBike(){
        station = new Station();
        station.setOccupancy(4);

        dock = new Dock();
        dock.setStation(station);
        dock.setDockId("ST001-D01");

        bike = new Bike();
        bike.setState(bikeState);
        bike.setBikeId("BK001");

        when(dockRepository.findById(dock.getDockId())).thenReturn(Optional.of(dock));

        spyService = Mockito.spy(bikeService);
        doReturn(bike).when(spyService).loadDockWithState(anyString());
    }

    private void mockStateResponse(UserStatus role, StateChangeStatus status, String msg) {
        when(bikeState.lockBike(
                eq(bike),
                eq(dock),
                eq(role)
        )).thenReturn(new StateChangeResponse(status, msg));
    }


}
