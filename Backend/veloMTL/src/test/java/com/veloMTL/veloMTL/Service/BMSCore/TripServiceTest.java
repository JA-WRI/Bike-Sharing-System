package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    BikeRepository bikeRepository;
    @Mock
    RiderRepository riderRepository;
    @Mock
    TripRepository tripRepository;
    @Mock
    OperatorRepository operatorRepository;

    @InjectMocks
    TripService tripService;

    Bike bike;
    Rider rider;
    Operator operator;
    Station station;
    Trip trip;

    @Test
    void create_trip_for_rider_test(){
        when(bikeRepository.findById("BK001")).thenReturn(Optional.of(bike));

        when(riderRepository.findById("rider1")).thenReturn(Optional.of(rider));

        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArgument(0));
        when(riderRepository.save(any(Rider.class))).thenReturn(rider);

        //act
        Trip createTrip = tripService.createTrip("BK001", "rider1", station);

        //assert
        assertNotNull(createTrip);
        assertEquals(bike, createTrip.getBike());
        assertEquals(rider.getEmail(),  createTrip.getUserEmail());
        assertEquals("Station1", createTrip.getOriginStation());
        assertNotNull(createTrip.getStartTime());

        verify(tripRepository).save(any(Trip.class));
        verify(riderRepository).save(rider);
    }

    @Test
    void create_trip_for_operator_test(){
        when(bikeRepository.findById("BK001")).thenReturn(Optional.of(bike));

        when(riderRepository.findById("op1")).thenReturn(Optional.empty());
        when(riderRepository.findByEmail("op1")).thenReturn(Optional.empty());
        when(operatorRepository.findById("op1")).thenReturn(Optional.of(operator));

        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArgument(0));
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);

        //act
        Trip createTrip = tripService.createTrip("BK001", "op1", station);

        //assert
        assertNotNull(createTrip);
        assertEquals(bike, createTrip.getBike());
        assertEquals(operator.getEmail(),  createTrip.getUserEmail());
        assertEquals("Station1", createTrip.getOriginStation());
        assertNotNull(createTrip.getStartTime());

        verify(tripRepository).save(any(Trip.class));
        verify(operatorRepository).save(operator);
    }

    @Test
    void end_trip_test(){
        trip = new Trip();
        trip.setBike(bike);
        Billing billing = new Billing();
        when(tripRepository.save(trip)).thenReturn(trip);

        //act
        TripDTO tripdto = tripService.endTrip(trip,station,billing);

        assertEquals(billing, trip.getBilling());
        verify(tripRepository).save(trip);
    }

    @Test
    void create_reservation_rider_test(){
        when(bikeRepository.findById("BK001")).thenReturn(Optional.of(bike));
        when(riderRepository.findById("rider1")).thenReturn(Optional.of(rider));

        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArgument(0));
        when(riderRepository.save(any(Rider.class))).thenReturn(rider);

        //act
        Trip createdTrip = tripService.createReserveTrip("BK001", "rider1", station);

        //assert
        assertNotNull(createdTrip);
        assertEquals(bike, createdTrip.getBike());
        assertEquals(rider.getEmail(), createdTrip.getUserEmail());
        assertEquals("Station1", createdTrip.getOriginStation());

        verify(tripRepository).save(any(Trip.class));
        verify(riderRepository).save(rider);
        verify(operatorRepository, never()).save(any());

    }

    @Test
    void create_reservation_operator_test(){
        when(bikeRepository.findById("BK001")).thenReturn(Optional.of(bike));
        when(riderRepository.findById("op1")).thenReturn(Optional.empty());
        when(riderRepository.findByEmail("op1")).thenReturn(Optional.empty());
        when(operatorRepository.findById("op1")).thenReturn(Optional.of(operator));

        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArgument(0));
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);

        // Act
        Trip createdTrip = tripService.createReserveTrip("BK001", "op1", station);

        // Assert
        assertNotNull(createdTrip);
        assertEquals(bike, createdTrip.getBike());
        assertEquals(operator.getEmail(), createdTrip.getUserEmail());
        assertEquals("Station1", createdTrip.getOriginStation());

        verify(tripRepository).save(any(Trip.class));
        verify(operatorRepository).save(operator);
        verify(riderRepository, never()).save(any());

    }

    @BeforeEach
    void setup(){
        bike = new Bike();
        bike.setBikeId("BK001");

        rider = new Rider();
        rider.setId("rider1");
        rider.setEmail("rider@example.com");

        station = new Station();
        station.setStationName("Station1");

        operator = new Operator();
        operator.setId("op1");
        operator.setEmail("op@example.com");
    }

}
