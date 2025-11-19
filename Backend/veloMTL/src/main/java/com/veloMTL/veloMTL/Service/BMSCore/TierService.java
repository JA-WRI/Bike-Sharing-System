package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import org.springframework.stereotype.Service;

@Service
public class TierService {

    public TierService() {
    }

    public LoyaltyTier resolveTier() {
        if (verifyGoldTier()) return LoyaltyTier.GOLD;
        if (verifySilverTier()) return LoyaltyTier.SILVER;
        if (verifyBronzeTier()) return LoyaltyTier.BRONZE;
        return LoyaltyTier.ENTRY;
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

