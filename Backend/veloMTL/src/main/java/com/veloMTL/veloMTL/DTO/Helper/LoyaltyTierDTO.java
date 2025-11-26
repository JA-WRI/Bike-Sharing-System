package com.veloMTL.veloMTL.DTO.Helper;

import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;

public class LoyaltyTierDTO {
    private final LoyaltyTier newTier;
    private final LoyaltyTier oldTier;
    private final boolean isTierChanged;

    public LoyaltyTierDTO(LoyaltyTier newTier, LoyaltyTier oldTier, boolean isTierChanged) {
        this.newTier = newTier;
        this.oldTier = oldTier;
        this.isTierChanged = isTierChanged;
    }

    public LoyaltyTier getNewTier() {
        return newTier;
    }

    public LoyaltyTier getOldTier() {
        return oldTier;
    }

    public boolean isTierChanged() {
        return isTierChanged;
    }
}
