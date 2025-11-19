package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Stations.StationState;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    StationRepository stationRepository;
    @Mock
    DockRepository dockRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    StationState stationState;
    Dock d1;
    Dock d2;



    @InjectMocks
    StationService stationService;
    Station station;
    StationService spyService;

    @Test
    void mark_station_out_of_service_success(){
        mockStationResponse_out_of_service(StateChangeStatus.SUCCESS, "Station was marked out of service");
        when(stationRepository.save(any())).thenReturn(station);
        when(dockRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // act
        ResponseDTO<StationDTO> response = spyService.markStationOutOfService("ST001", UserStatus.OPERATOR);

        // assert
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Station was marked out of service", response.getMessage());

        // station saved
        verify(stationRepository).save(station);

        // all docks saved
        verify(dockRepository).save(d1);
        verify(dockRepository).save(d2);
        verify(dockRepository, times(2)).save(any(Dock.class));
    }

    @Test
    void mark_station_out_of_service_invalid(){
        mockStationResponse_out_of_service(StateChangeStatus.FAILURE, "Take all bikes out of station");

        // act
        ResponseDTO<StationDTO> response = spyService.markStationOutOfService("ST001", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("Take all bikes out of station", response.getMessage());

        verify(stationRepository, never()).save(station);
        verify(dockRepository,never()).save(d1);
        verify(dockRepository, never()).save(d2);
    }

    @Test
    void restore_station_success(){
        mockStationResponse_restore(StateChangeStatus.SUCCESS, "Station status is restored");
        when(stationRepository.save(any())).thenReturn(station);
        when(dockRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // act
        ResponseDTO<StationDTO> response = spyService.restoreStation("ST001", UserStatus.OPERATOR);
        // assert
        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Station status is restored", response.getMessage());

        // station saved
        verify(stationRepository).save(station);

        // all docks saved
        verify(dockRepository).save(d1);
        verify(dockRepository).save(d2);
        verify(dockRepository, times(2)).save(any(Dock.class));
    }

    @Test
    void restore_station_invalid(){
        mockStationResponse_restore(StateChangeStatus.FAILURE, "Station is already in service");

        // act
        ResponseDTO<StationDTO> response = spyService.restoreStation("ST001", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("Station is already in service", response.getMessage());

        verify(stationRepository, never()).save(station);
        verify(dockRepository,never()).save(d1);
        verify(dockRepository, never()).save(d2);
    }


    @BeforeEach
    void setup(){
        // Mock station
        station = new Station();
        station.setStationState(stationState);

        // Mock docks belonging to station
        d1 = new Dock();
        d2 = new Dock();
        station.setDocks(List.of(d1, d2));

        spyService = Mockito.spy(stationService);
        doReturn(station).when(spyService).loadStationWithState("ST001");
    }

    private void mockStationResponse_out_of_service(StateChangeStatus status, String msg){
        when(stationState.markStationOutOfService(
                eq(station)
        )).thenReturn(new StateChangeResponse(status, msg));
    }

    private void mockStationResponse_restore(StateChangeStatus status, String msg){
        when(stationState.restoreStation(
                eq(station)
        )).thenReturn(new StateChangeResponse(status, msg));
    }

}
