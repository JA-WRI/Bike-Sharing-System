package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BikeServiceTest_UnlockBike {

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
    void unlockBike_success_forRiders(){

        mockStateResponse(UserStatus.RIDER, StateChangeStatus.SUCCESS, "Bike has been unlocked", "user1");
        when(bikeRepository.save(any())).thenReturn(bike);

        //act
        ResponseDTO<BikeDTO> response = spyService.unlockBike("bike123", "user1", UserStatus.RIDER);

        //assert
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Bike has been unlocked", response.getMessage());

        // Verify occupancy updated
        verify(stationService).updateStationOccupancy(station, 9);
        // Verify docking removal
        verify(dockRepository).save(dock);
        // Verify bike saved
        verify(bikeRepository).save(bike);
        // Rider should trigger trip creation
        verify(tripService).createTrip("bike123", "user1", station);

    }
    @Test
    void unlockBike_invalid_forRiders(){
        mockStateResponse(UserStatus.RIDER, StateChangeStatus.FAILURE, "You cannot unlock this bike", "user1");

        //act
        ResponseDTO<BikeDTO> response = spyService.unlockBike("bike123", "user1", UserStatus.RIDER);

        //assert
        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("You cannot unlock this bike", response.getMessage());

        // Verify NO side effects happened
        verify(stationService, never()).updateStationOccupancy(any(), anyInt());
        verify(tripService, never()).createTrip(any(), any(), any());
        verify(bikeRepository, never()).save(any());
        verify(dockRepository, never()).save(any());
    }

    @Test
    void unlockBike_success_forOperator(){
        mockStateResponse(UserStatus.OPERATOR, StateChangeStatus.SUCCESS, "Bike is out of service and undocked", "op1");
        when(bikeRepository.save(any())).thenReturn(bike);

        ResponseDTO<BikeDTO> response = spyService.unlockBike("bike123", "op1", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Bike is out of service and undocked", response.getMessage());

        verify(stationService).updateStationOccupancy(station, 9);
        verify(dockRepository).save(dock);
        verify(bikeRepository).save(bike);

        // Operators DO NOT create trips
        verify(tripService, never()).createTrip(any(), any(), any());
    }

    @Test
    void unlockBike_invalid_forOperators(){
        mockStateResponse(UserStatus.OPERATOR, StateChangeStatus.FAILURE, "Cannot unlock this bike", "op1");

        ResponseDTO<BikeDTO> response = spyService.unlockBike("bike123", "op1", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("Cannot unlock this bike", response.getMessage());

        // Ensure nothing is saved or changed
        verify(stationService, never()).updateStationOccupancy(any(), anyInt());
        verify(dockRepository, never()).save(any());
        verify(bikeRepository, never()).save(any());
        verify(tripService, never()).createTrip(any(), any(), any());
    }

    @BeforeEach
    void setupMocks(){
        //setup for the test (creating station, dock, and bike)
        station = new Station();
        station.setOccupancy(10);

        dock = new Dock();
        dock.setStation(station);

        bike = new Bike();
        bike.setDock(dock);
        bike.setState(bikeState);

        //creating a bike service using spy, so I can override the loadDockWithState
        //method since I do not actually want to search in the DB
        spyService = Mockito.spy(bikeService);
        doReturn(bike).when(spyService).loadDockWithState("bike123");

    }

    private void mockStateResponse(UserStatus role, StateChangeStatus status, String msg, String userId) {
        when(bikeState.unlockBike(
                eq(bike),
                eq(dock),
                eq(role),
                any(LocalDateTime.class),
                eq(userId)
        )).thenReturn(new StateChangeResponse(status, msg));
    }
}
