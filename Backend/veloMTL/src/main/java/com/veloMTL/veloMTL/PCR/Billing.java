package com.veloMTL.veloMTL.PCR;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "Billing")
public class Billing {
    @Id
    private String riderID;
    private String description;
    private double cost;
    private LocalDateTime dateTransaction;
    //private String paymentMethod;

    public Billing(String riderID, String description, double cost, LocalDateTime dateTransaction) {
        this.riderID = riderID;
        this.description = description;
        this.cost = cost;
        this.dateTransaction = dateTransaction;
        //this.paymentMethod = paymentMethod;
    }

    public String getRiderID() {
        return riderID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDateTime getDateTransation() {
        return dateTransaction;
    }

    public void setDateTransation(LocalDateTime dateTransation) {
        this.dateTransaction = dateTransation;
    }

//    public String getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public void setPaymentMethod(String paymentMethod) {
//        this.paymentMethod = paymentMethod;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
