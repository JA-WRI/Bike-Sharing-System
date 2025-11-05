package com.veloMTL.veloMTL.Service.BMSCore;


import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final double rate = 0.10;
    private final RiderRepository riderRepository;

    public PaymentService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public long tripDuration (Trip trip){
    LocalDateTime startTime  = trip.getStartTime();
    LocalDateTime endTime = trip.getEndTime();
    Duration duration = Duration.between(startTime, endTime);
    return duration.toMinutes();
}

public double priceCalculation(long duration){
     return rate * duration;
}

public void pay (String riderId, Trip trip){
    Rider rider = riderRepository.findById(riderId)
            .orElseThrow(() -> new RuntimeException("Rider not found with ID: " + riderId));
    long duration = tripDuration(trip);
    double price = priceCalculation(duration);


}

}
