package com.veloMTL.veloMTL.Service.History;

import com.veloMTL.veloMTL.DTO.History.TripHistoryDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryServiceTest {

    static class TestRider extends Rider {
        public TestRider(String email) {
            this.setEmail(email);
        }
    }

    static class TestOperator extends Operator {
        public TestOperator(String email) {
            this.setEmail(email);
        }
    }

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private BillingRepository billingRepository;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Trip buildTrip(String bikeId, String userEmail) {
        Trip trip = new Trip();

        Bike bike = new Bike();
        bike.setBikeId(bikeId);
        trip.setBike(bike);

        trip.setUserEmail(userEmail);
        trip.setTripId("T123");
        trip.setStartTime(LocalDateTime.now());
        trip.setEndTime(LocalDateTime.now());
        trip.setOriginStation("Station-A");
        trip.setArrivalStation("Station-B");

        return trip;
    }

    @Test
    void fetchRiderTrips_shouldReturnTrips_whenRiderFoundById() {
        String userId = "123";
        Rider mockRider = new TestRider("rider@mail.com");

        Trip t1 = buildTrip("B1", "rider@mail.com");
        Trip t2 = buildTrip("B2", "rider@mail.com");

        when(riderRepository.findById(userId)).thenReturn(Optional.of(mockRider));
        when(tripRepository.fetchTripsByUserId("rider@mail.com"))
                .thenReturn(List.of(t1, t2));

        List<TripHistoryDTO> result = historyService.fetchRiderTrips(userId);

        assertEquals(2, result.size());
        verify(riderRepository).findById(userId);
    }

    @Test
    void fetchRiderTrips_shouldCheckOperatorId_ifRiderNotFound() {
        String userId = "456";
        Operator mockOperator = new TestOperator("operator@mail.com");

        when(riderRepository.findById(userId)).thenReturn(Optional.empty());
        when(operatorRepository.findById(userId)).thenReturn(Optional.of(mockOperator));
        when(tripRepository.fetchTripsByUserId("operator@mail.com")).thenReturn(List.of());

        List<TripHistoryDTO> result = historyService.fetchRiderTrips(userId);

        assertNotNull(result);
        verify(operatorRepository).findById(userId);
    }

    @Test
    void fetchRiderTrips_shouldCheckRiderEmail_ifNotFoundByIds() {
        String email = "rider@mail.com";
        Rider mockRider = new TestRider(email);

        when(riderRepository.findById(email)).thenReturn(Optional.empty());
        when(operatorRepository.findById(email)).thenReturn(Optional.empty());
        when(riderRepository.findByEmail(email)).thenReturn(Optional.of(mockRider));
        when(tripRepository.fetchTripsByUserId(email)).thenReturn(List.of());

        List<TripHistoryDTO> result = historyService.fetchRiderTrips(email);

        assertNotNull(result);
        verify(riderRepository).findByEmail(email);
    }

    @Test
    void fetchRiderTrips_shouldCheckOperatorEmail_ifRiderEmailNotFound() {
        String email = "operator@mail.com";
        Operator mockOperator = new TestOperator(email);

        when(riderRepository.findById(email)).thenReturn(Optional.empty());
        when(operatorRepository.findById(email)).thenReturn(Optional.empty());
        when(riderRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(operatorRepository.findByEmail(email)).thenReturn(Optional.of(mockOperator));
        when(tripRepository.fetchTripsByUserId(email)).thenReturn(List.of());

        List<TripHistoryDTO> result = historyService.fetchRiderTrips(email);

        assertNotNull(result);
        verify(operatorRepository).findByEmail(email);
    }

    @Test
    void fetchRiderTrips_shouldThrowException_whenUserNotFound() {
        String unknown = "unknown@mail.com";

        when(riderRepository.findById(unknown)).thenReturn(Optional.empty());
        when(operatorRepository.findById(unknown)).thenReturn(Optional.empty());
        when(riderRepository.findByEmail(unknown)).thenReturn(Optional.empty());
        when(operatorRepository.findByEmail(unknown)).thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class, () ->
                        historyService.fetchRiderTrips(unknown));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void fetchAllTrips_shouldReturnProcessedTrips() {
        Trip trip = buildTrip("B100", "test@mail.com");

        when(tripRepository.findAll()).thenReturn(List.of(trip));

        List<TripHistoryDTO> result = historyService.fetchAllTrips();

        assertEquals(1, result.size());
        verify(tripRepository).findAll();
    }
}
