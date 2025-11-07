package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "Billing")
public class Billing {
    @Id
    private String billID;
    private String description;
    private LocalDateTime dateTransaction;
    private String riderID;
    private String bikeID;
    private String originStation;
    private  String arrivalStation;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double ratePerMinute;
    private double cost;

    //private String tripId;


    public Billing() {}

    public Billing(String description, LocalDateTime dateTransaction, String riderID, String bikeID, String originStation, String arrivalStation, LocalDateTime startDate, LocalDateTime endDate, double ratePerMinute, double cost) {

        this.description = description;
        this.dateTransaction = dateTransaction;
        this.riderID = riderID;
        this.bikeID = bikeID;
        this.originStation = originStation;
        this.arrivalStation = arrivalStation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ratePerMinute = ratePerMinute;
        this.cost = cost;
    }

    public Billing( String description,LocalDateTime dateTransaction, String riderID,double cost) {
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

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getOriginStation() {
        return originStation;
    }

    public void setOriginStation(String originStation) {
        this.originStation = originStation;
    }

    public String getBikeID() {
        return bikeID;
    }

    public void setBikeID(String bikeID) {
        this.bikeID = bikeID;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getRatePerMinute() {
        return ratePerMinute;
    }

    public void setRatePerMinute(double ratePerMinute) {
        this.ratePerMinute = ratePerMinute;
    }
    //    public String getTrip() {
//        return tripId;
//    }
//
//    public void setTrip(String trip) {
//        this.tripId = tripId;
//    }
}
