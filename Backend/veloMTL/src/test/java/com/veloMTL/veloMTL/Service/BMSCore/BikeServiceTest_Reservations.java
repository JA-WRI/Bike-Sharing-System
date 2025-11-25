package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.State.Bikes.AvailableBikeState;
import com.veloMTL.veloMTL.Patterns.State.Bikes.BikeState;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.Service.PRC.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BikeServiceTest_Reservations {
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
    @Mock
    TierService tierService;

    @InjectMocks
    BikeService bikeService;

    Station station;
    Dock dock;
    Bike bike;
    Rider user;
    BikeService spyService;


    //create test for reservation of bike success
    @Test
    void reserveBike_success () {
//        mockStateResponse(StateChangeStatus.SUCCESS, "Bike has been unlocked", "user1");
        dock.setDockId("dock1");
        user = new Rider("mockUser", "user@email.com", "123");
        when(dockRepository.findById("dock1")).thenReturn(Optional.of(dock));
        when(tierService.fetchUserTier(anyString())).thenReturn(LoyaltyTier.ENTRY);
        when(bikeRepository.save(any())).thenReturn(bike);
        when(riderRepository.findById("user1")).thenReturn(Optional.of(user));
        //act
        ResponseDTO<BikeDTO> response = spyService.reserveBike("bike123", "user1", dock.getDockId(),
                LocalDateTime.now(), UserStatus.RIDER);

        //assert
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Bike has been reserved", response.getMessage());

        // Verify bike saved
        verify(bikeRepository).save(bike);
        verify(tripService).createReserveTrip("bike123", "user1", station);
        assertEquals(BikeStatus.RESERVED, bike.getBikeStatus());
        assertNotNull(bike.getReserveDate());
        assertEquals("user1", bike.getReserveUser());
    }

    //create test for reservation bike fail
    @Test
    void reserveBike_fail() {
        assertThrows(RuntimeException.class, () -> spyService.reserveBike("bike123", "user1", dock.getDockId(),
                LocalDateTime.now(), UserStatus.RIDER));
        verify(bikeRepository, never()).save(any());
        assertEquals(AvailableBikeState.class, bike.getState().getClass());
        assertNull(bike.getReserveDate());
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
        bike.setState(new AvailableBikeState());

        //creating a bike service using spy, so I can override the loadDockWithState
        //method since I do not actually want to search in the DB
        spyService = Mockito.spy(bikeService);
        doReturn(bike).when(spyService).loadDockWithState("bike123");

    }

}
