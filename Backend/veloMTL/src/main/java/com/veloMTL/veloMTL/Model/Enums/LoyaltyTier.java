package com.veloMTL.veloMTL.Model.Enums;

public enum LoyaltyTier {
    ENTRY("Entry", 0, 0),
    BRONZE("Bronze", 5, 0),
    SILVER("Silver", 10, 2),
    GOLD("Gold", 15, 5);

    private final String text;
    private final int discount;
    private final int extraHold;

    LoyaltyTier(String text, int discount, int extraHold) {
        this.text = text;
        this.discount = discount;
        this.extraHold = extraHold;
    }

    public String getText() {
        return this.text;
    }

    public int getDiscount() {
        return this.discount;
    }

    public int getExtraHold() {
        return this.extraHold;
    }
}