package com.veloMTL.veloMTL.Patterns.Strategy;

import com.veloMTL.veloMTL.DTO.BMSCore.PaymentMethodDTO;

import java.time.LocalDate;

public class Visa implements  PaymentMethod{
    @Override
    public void processPayment(double amount) {
        System.out.println("Payment Confirmed\n" +
                "Date: " + LocalDate.now() + "\n" +
                "Payment method: Visa" + "\n" +
                "Total amount: " + amount);
    }
}
