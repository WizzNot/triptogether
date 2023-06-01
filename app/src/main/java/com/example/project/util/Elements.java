package com.example.project.util;

public class Elements {
    public String coast;
    public String onePoint;
    public String twoPoint;
    public String time;
    public String telephone;


    public Elements(String coast, String onePoint, String twoPoint, String time, String telephone) {
        this.coast = coast;
        this.onePoint = onePoint;
        this.twoPoint = twoPoint;
        this.time = time;
        this.telephone = telephone;
    }

    public Elements() {
        coast = "51";
        onePoint = "Первомайская 21";
        twoPoint = "Пинского 2";
        time = "34";
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public String getOnePoint() {
        return onePoint;
    }

    public void setOnePoint(String onePoint) {
        this.onePoint = onePoint;
    }

    public String getTwoPoint() {
        return twoPoint;
    }

    public void setTwoPoint(String twoPoint) {
        this.twoPoint = twoPoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
