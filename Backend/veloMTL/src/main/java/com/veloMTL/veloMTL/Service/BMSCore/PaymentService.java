package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;

import java.time.Duration;
import java.time.LocalDateTime;

public class PaymentService {
    private final double rate = 0.10;

public long tripDuration (TripDTO tripDTO){
    LocalDateTime startTime  = tripDTO.getStartTime();
    LocalDateTime endTime = tripDTO.getEndTime();
    Duration duration = Duration.between(startTime, endTime);
    return duration.toMinutes();
}

public double priceCalculation(long duration){
     return rate * duration;
}


}
