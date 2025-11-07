package com.veloMTL.veloMTL.Controller.History;

import com.veloMTL.veloMTL.DTO.History.TripHistoryDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Service.History.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<TripHistoryDTO>> fetchRiderTrips() {
        User currentUser = historyService.fetchCurrentUser();
        ResponseEntity<List<TripHistoryDTO>> response;
        if (currentUser instanceof Operator) {
            response = ResponseEntity.ok(historyService.fetchAllTrips());
        } else if (currentUser instanceof Rider) {
            response = ResponseEntity.ok(historyService.fetchRiderTrips(currentUser.getId()));
            System.out.println(response);
        } else {
            throw new RuntimeException("Current User does not have a Role!");
        }
        return response;
    }


}
