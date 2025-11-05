package com.veloMTL.veloMTL.Controller.History;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Service.History.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/history")
public class HistoryController {
    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/rider")
    public ResponseEntity<List<Trip>> fetchRiderTrips() {
        Rider currentRider = historyService.fetchCurrentRider();
        return ResponseEntity.ok(historyService.fetchRiderTrips(currentRider.getId()));
    }


}
