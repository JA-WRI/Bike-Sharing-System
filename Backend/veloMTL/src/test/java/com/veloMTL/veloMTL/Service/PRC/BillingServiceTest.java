package com.veloMTL.veloMTL.Service.PRC;

import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.Service.BMSCore.TierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    TierService tierService;

    @InjectMocks
    private BillingService billingService;

    @Mock
    private Trip trip;

    @Mock
    private Plan plan;

    @Mock
    private Station station;

    @Mock
    private com.veloMTL.veloMTL.Model.BMSCore.Bike bike;

    @Mock
    private Rider rider;

    @Mock
    private Operator operator;

    @BeforeEach
    void setup() {
        lenient().when(rider.getId()).thenReturn("1111");
        lenient().when(rider.getEmail()).thenReturn("test@example.com");
        lenient().when(rider.getFlexDollars()).thenReturn(10.0);
        lenient().when(rider.getPlan()).thenReturn(plan);
    }


    @Test
    void pay() {
        LocalDateTime start = LocalDateTime.now().minusMinutes(20);
        LocalDateTime end = LocalDateTime.now();

        // Trip info
        when(trip.getUserEmail()).thenReturn("test@example.com");
        when(trip.getStartTime()).thenReturn(start);
        when(trip.getEndTime()).thenReturn(end);
        when(trip.getOriginStation()).thenReturn("StationA");
        when(trip.getArrivalStation()).thenReturn("StationB");

        // Bike info
        when(trip.getBike()).thenReturn(bike);
        when(bike.getBikeId()).thenReturn("Bike001");
        when(bike.getBikeType()).thenReturn("electric");

        // Repository mocks
        when(riderRepository.findByEmail("test@example.com")).thenReturn(Optional.of(rider));
        when(riderRepository.findById("1111")).thenReturn(Optional.of(rider));
        when(stationRepository.findByStationName("StationB")).thenReturn(Optional.of(station));

        // Station mock
        when(station.getOccupancy()).thenReturn(5);
        when(station.getCapacity()).thenReturn(10);

        // Plan mock
        when(plan.getRatebyMinute()).thenReturn(0.50);
        doReturn(3.75).when(plan).calculateTripCost(
                anyLong(),
                anyBoolean(),
                anyDouble(),
                any(RiderRepository.class),
                any(OperatorRepository.class),
                eq("1111"),
                anyInt()
        );

        // Captor for saved billing
        ArgumentCaptor<Billing> billCaptor = ArgumentCaptor.forClass(Billing.class);

        // Execute method
        Billing result = billingService.pay(trip);

        verify(billingRepository).save(billCaptor.capture());
        Billing savedBill = billCaptor.getValue();

        // Assertions
        assertNotNull(result);
        assertEquals("Trip", savedBill.getDescription());
        assertEquals("Bike001", savedBill.getBikeID());
        assertEquals("StationA", savedBill.getOriginStation());
        assertEquals("StationB", savedBill.getArrivalStation());
        assertEquals(0.50, savedBill.getRatePerMinute());
        assertEquals(3.75, savedBill.getCost());

        verify(riderRepository).save(any(Rider.class));
    }

    @Test
    void generateMonthlyBillingRiders() {
        when(rider.getPlan()).thenReturn(plan);
        when(plan.getBaseFee()).thenReturn(20);

        ArgumentCaptor<Billing> billCaptor = ArgumentCaptor.forClass(Billing.class);

        Billing result = billingService.generateMonthlyBillingRiders(rider);

        verify(billingRepository).save(billCaptor.capture());
        Billing savedBill = billCaptor.getValue();

        assertNotNull(result);
        assertEquals("Monthly Base Fee", savedBill.getDescription());
        assertEquals("1111", savedBill.getRiderID());
        assertEquals(20, savedBill.getCost());

        // Use duration check instead of direct LocalDateTime equality
        assertTrue(Duration.between(savedBill.getDateTransaction(), LocalDateTime.now()).toSeconds() < 1);
    }


    @Test
    void generateMonthlyBillingOperator() {
        when(operator.getId()).thenReturn("op123");
        when(operator.getPlan()).thenReturn(plan); // <-- this is critical
        when(plan.getBaseFee()).thenReturn(50);

        ArgumentCaptor<Billing> billCaptor = ArgumentCaptor.forClass(Billing.class);

        Billing result = billingService.generateMonthlyBillingOperator(operator);

        verify(billingRepository).save(billCaptor.capture());
        Billing savedBill = billCaptor.getValue();

        assertNotNull(result);
        assertEquals("Monthly Base Fee", savedBill.getDescription());
        assertEquals("op123", savedBill.getRiderID());
        assertEquals(50, savedBill.getCost());

        assertTrue(Duration.between(savedBill.getDateTransaction(), LocalDateTime.now()).toSeconds() < 1);
    }


}
