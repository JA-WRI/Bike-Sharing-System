package com.veloMTL.veloMTL.DTO.BMSCore;

import java.util.Date;

public class PaymentMethodDTO {
    private String paymentMethod;
    private String nameOnCard;
    private String cardNumber;
    private String cvcNumber;
    private String expirationDate;
    //private double amount;

    public PaymentMethodDTO(String paymentMethod, String nameOnCard, String cardNumber, String cvcNumber, String expirationDate) {
        this.paymentMethod = paymentMethod;
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.cvcNumber = cvcNumber;
        this.expirationDate = expirationDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvcNumber() {
        return cvcNumber;
    }

    public void setCvcNumber(String cvcNumber) {
        this.cvcNumber = cvcNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
