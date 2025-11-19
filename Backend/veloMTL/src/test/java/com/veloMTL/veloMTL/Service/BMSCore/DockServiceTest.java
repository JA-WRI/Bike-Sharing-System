package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.DockState;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
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
public class DockServiceTest {

    @Mock
    BikeRepository bikeRepository;
    @Mock
    DockRepository dockRepository;
    @Mock
    DockState dockState;

    @InjectMocks
    DockService dockService;
    Dock dock;
    DockService spyService;

    @Test
    void mark_station_out_of_service_test_success(){
        mockDockStateResponse_out_of_service(StateChangeStatus.SUCCESS, "Dock is set to out of service");
        when(dockRepository.save(any())).thenReturn(dock);

        ResponseDTO<DockDTO> response = spyService.markDockOutOfService("dock123", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Dock is set to out of service", response.getMessage());

        verify(dockRepository).save(dock);

    }
    @Test
    void mark_station_out_of_service_test_invalid(){
        mockDockStateResponse_out_of_service(StateChangeStatus.INVALID_TRANSITION, "Bike needs to be removed first");

        ResponseDTO<DockDTO> response = spyService.markDockOutOfService("dock123", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.INVALID_TRANSITION, response.getStatus());
        assertEquals("Bike needs to be removed first", response.getMessage());

        verify(dockRepository, never()).save(any());

    }
    @Test
    void restore_dock_success(){
        mockDockStateResponse_restore_dock(StateChangeStatus.SUCCESS, "Dock is in service");

        ResponseDTO<DockDTO> response = spyService.restoreDockStatus("dock123", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.SUCCESS, response.getStatus());
        assertEquals("Dock is in service", response.getMessage());

        verify(dockRepository).save(dock);

    }
    @Test
    void restore_dock_invalid(){
        mockDockStateResponse_restore_dock(StateChangeStatus.FAILURE, "Cannot mark out of service");

        ResponseDTO<DockDTO> response = spyService.restoreDockStatus("dock123", UserStatus.OPERATOR);

        assertEquals(StateChangeStatus.FAILURE, response.getStatus());
        assertEquals("Cannot mark out of service", response.getMessage());

        verify(dockRepository, never()).save(dock);
    }

    //create test for dock reservation success
    //create test for dock reservation fail

    @BeforeEach
    void setup(){
        dock = new Dock();
        dock.setState(dockState);
        spyService = Mockito.spy(dockService);
        doReturn(dock).when(spyService).loadDockWithState("dock123");
    }

    private void mockDockStateResponse_out_of_service(StateChangeStatus status, String msg){
        when(dockState.markDockOutOfService(
                eq(dock)
        )).thenReturn(new StateChangeResponse(status, msg));
    }
    private void mockDockStateResponse_restore_dock(StateChangeStatus status, String msg){
        when(dockState.restoreService(eq(dock))).
                thenReturn(new StateChangeResponse(status, msg));
    }
}
