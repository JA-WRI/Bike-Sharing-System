package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingScheduler {

    private final BillingService billingService;
    private final RiderRepository riderRepository;

    public BillingScheduler(BillingService billingService, RiderRepository riderRepository) {
        this.billingService = billingService;
        this.riderRepository = riderRepository;
    }

    // Runs every month on the 1st at midnight
    @Scheduled(cron = "0 0 0 1 * ?")
    public void billAllRidersMonthly() {
        List<Rider> riders = riderRepository.findAll();
        for (Rider rider : riders) {
            try {
                billingService.generateMonthlyBilling(rider);
                System.out.println("Billed rider " + rider.getId() + " for monthly base fee.");
            } catch (Exception e) {
                System.err.println("Failed to bill rider " + rider.getId() + ": " + e.getMessage());
            }
        }
    }
}
