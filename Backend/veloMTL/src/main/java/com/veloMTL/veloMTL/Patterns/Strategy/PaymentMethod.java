package com.veloMTL.veloMTL.Patterns.Strategy;

import com.veloMTL.veloMTL.DTO.BMSCore.PaymentMethodDTO;

public interface PaymentMethod {
    void processPayment(double amount, PaymentMethodDTO paymentMethodDTO);
}
