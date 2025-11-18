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
    private BikeRepository bikeRepository;
    @Mock
    private DockRepository dockRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private StationService stationService;
    @Mock
    private TripService tripService;
    @Mock
    private BikeState bikeState;
    @Mock
    private RiderRepository riderRepository;
    @Mock
    private BillingService billingService;
    @Mock
    private TimerService timerService;

    @InjectMocks
    private BikeService bikeService;


    @Test
    void unlockBike_success_forRider_bikeState_is_available(){
        //setup for the test (creating station, dock, and bike)
        Station station = new Station();
        station.setOccupancy(10);

        Dock dock = new Dock();
        dock.setStation(station);

        Bike bike = new Bike();
        bike.setDock(dock);
        bike.setState(bikeState);

        //creating a bike service using spy, so I can override the loadDockWithState
        //method since I do not actually want to search in the DB
        BikeService spyService = Mockito.spy(bikeService);
        doReturn(bike).when(spyService).loadDockWithState("bike123");

        // when the state is called, simulate success
        StateChangeResponse mockResponse = new StateChangeResponse(
                StateChangeStatus.SUCCESS,
                "Bike has been unlocked"
        );

        //returning a success message when fake calling the unlockBike from (Available state)
        when(bikeState.unlockBike(
                eq(bike),
                eq(dock),
                eq(UserStatus.RIDER),
                any(LocalDateTime.class),
                eq("user1")
        )).thenReturn(mockResponse);

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
    //create tests to make sure that if unlock was performed on the wrong state that nothing happens (fail scenario)

    //create testing method for the operator to unlock the bike success

    //create test for operator unlocking bike fail
}
