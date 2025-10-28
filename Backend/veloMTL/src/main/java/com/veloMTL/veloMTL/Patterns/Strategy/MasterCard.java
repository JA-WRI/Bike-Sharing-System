package com.veloMTL.veloMTL.Patterns.Strategy;

import java.time.LocalDate;


public class MasterCard implements PaymentMethod{

    @Override
    public void processPayment(double amount) {

        System.out.println("Payment Confirmed\n" +
                "Date: " + LocalDate.now() +
                "\n");
    }
}
