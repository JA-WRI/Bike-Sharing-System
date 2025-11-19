package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Repository.BMSCore.*;
import org.springframework.stereotype.Service;

@Service
public class TierService {
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;

    public TierService(TripRepository tripRepository, ReservationRepository reservationRepository) {
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
    }

    public LoyaltyTier resolveTier(String userEmail) {
        // can do chain of responsibility pattern
        if (!verifyBronzeTier(userEmail)) return LoyaltyTier.ENTRY;
        if (!verifySilverTier(userEmail)) return LoyaltyTier.BRONZE;
        if (!verifyGoldTier(userEmail)) return LoyaltyTier.SILVER;
        return LoyaltyTier.GOLD;
    }

    private boolean verifyBronzeTier(String userEmail) {
        return false;
    }

    private boolean verifySilverTier(String userEmail) {
        return false;
    }

    private boolean verifyGoldTier(String userEmail) {
        return false;
    }
}

