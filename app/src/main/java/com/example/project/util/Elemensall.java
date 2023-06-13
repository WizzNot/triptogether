package com.example.project.util;

public class Elemensall {
    public String driversName;
    public String fromAddresses;
    public String toAddresses;
    public String prices;
    public String countPass;
    public String verification;
    public String data;
    public String comment;


    public Elemensall(String driversName, String fromAddresses, String toAddresses, String prices, String countPass, String verification, String data, String com) {
        this.driversName = driversName;
        this.fromAddresses = fromAddresses;
        this.toAddresses = toAddresses;
        this.prices = prices;
        this.countPass = countPass;
        this.verification = verification;
        this.data = data;
        this.comment = com;

    }

    public Elemensall() {
        this.driversName = "lol";
        this.fromAddresses = "lol";
        this.toAddresses = "lol";
        this.prices = "lol";
        this.countPass = "lol";
        this.comment = "empty";
    }

    public String getDriversName() {
        return driversName;
    }

    public void setDriversName(String driversName) {
        this.driversName = driversName;
    }

    public String getFromAddresses() {
        return fromAddresses;
    }

    public void setFromAddresses(String fromAddresses) {
        this.fromAddresses = fromAddresses;
    }

    public String getToAddresses() {
        return toAddresses;
    }
    public String getComment() {return comment;}
    public void setComment(String s) {this.comment = s;}

    public void setToAddresses(String toAddresses) {
        this.toAddresses = toAddresses;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getCountPass() {
        return countPass;
    }

    public void setCountPass(String countPass) {
        this.countPass = countPass;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}