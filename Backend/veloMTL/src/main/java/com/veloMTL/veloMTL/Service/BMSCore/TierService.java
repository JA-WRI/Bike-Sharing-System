package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Repository.BMSCore.*;
import com.veloMTL.veloMTL.Service.PRC.BillingService;
import org.springframework.stereotype.Service;

@Service
public class TierService {
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;

    public TierService(TripRepository tripRepository, ReservationRepository reservationRepository) {
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
    }

    public LoyaltyTier resolveTier(String userId) {
        // can do chain of responsibility pattern
        if (!verifyBronzeTier()) return LoyaltyTier.ENTRY;
        if (!verifySilverTier()) return LoyaltyTier.BRONZE;
        if (!verifyGoldTier()) return LoyaltyTier.SILVER;
        return LoyaltyTier.GOLD;
    }

    private boolean verifyBronzeTier() {
        return false;
    }

    private boolean verifySilverTier() {
        return false;
    }

    private boolean verifyGoldTier() {
        return false;
    }
}

