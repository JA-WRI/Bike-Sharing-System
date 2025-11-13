package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.Service.PRC.BillingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingScheduler {

    private final BillingService billingService;
    private final RiderRepository riderRepository;
    private final OperatorRepository operatorRepository;


    public BillingScheduler(BillingService billingService, RiderRepository riderRepository, OperatorRepository operatorRepository) {
        this.billingService = billingService;
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
    }

    // Runs every month on the 1st at midnight
    @Scheduled(cron = "0 0 0 1 * ?")
    //@Scheduled(cron = "0 */1 * * * *") // every 1 minute
    public void billAllRidersMonthly() {

        List<Rider> riders = riderRepository.findAll();
        for (Rider rider : riders) {
            try {
                billingService.generateMonthlyBillingRiders(rider);
                System.out.println("Billed rider " + rider.getId() + " for monthly base fee.");
            } catch (Exception e) {
                System.err.println("Failed to bill rider " + rider.getId() + ": " + e.getMessage());
            }
        }


    }

    @Scheduled(cron = "0 0 0 1 * ?")
    //@Scheduled(cron = "0 */1 * * * *") // every 1 minute
    public void billAllOperatorsMonthly() {

        List<Operator> operators = operatorRepository.findAll();
        for (Operator operator : operators) {
            try {
                if(operator.getRole().contains("RIDER")) {
                    billingService.generateMonthlyBillingOperator(operator);
                    System.out.println("Billed rider " + operator.getId() + " for monthly base fee.");
                }
            } catch (Exception e) {
                System.err.println("Failed to bill rider " + operator.getId() + ": " + e.getMessage());
            }
        }


    }
}
