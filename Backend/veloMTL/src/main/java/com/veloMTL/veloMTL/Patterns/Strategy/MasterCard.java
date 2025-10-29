package com.veloMTL.veloMTL.Patterns.Strategy;

import com.veloMTL.veloMTL.DTO.BMSCore.PaymentMethodDTO;

import java.time.LocalDate;


public class MasterCard implements PaymentMethod{

    @Override
    public void processPayment(double amount, PaymentMethodDTO paymentMethodDTO) {

        System.out.println("Payment Confirmed\n" +
                "Date: " + LocalDate.now() + "\n" +
                "Payment method: " + paymentMethodDTO.getPaymentMethod() +"\n" +
                "Cardholder name: " + paymentMethodDTO.getNameOnCard() + "\n" +
                "Total amount: " + amount);
    }
}
