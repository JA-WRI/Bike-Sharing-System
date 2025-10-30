package com.veloMTL.veloMTL.Patterns.Strategy;

import com.veloMTL.veloMTL.DTO.BMSCore.PaymentMethodDTO;

import java.time.LocalDate;


public class MasterCard implements PaymentMethod{

    @Override
    public void processPayment(double amount) {

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n" +
                "Payment Confirmed\n" +
                "Date: " + LocalDate.now() + "\n" +
                "Payment method: Mastercard" +"\n" +
                "Total amount: " + amount);
    }
}
