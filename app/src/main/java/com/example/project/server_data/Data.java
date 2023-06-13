package com.example.project.server_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    @SerializedName("reg")
    @Expose
    private Boolean reg;
    @SerializedName("login")
    @Expose
    private Boolean login;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("patronymic")
    @Expose
    private String patronymic;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("driverver")
    @Expose
    private String driverver;
    @SerializedName("compver")
    @Expose
    private String compver;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("second_password")
    @Expose
    private String secondPassword;
    @SerializedName("countpass")
    @Expose
    private String countpass;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("companion")
    @Expose
    private String companion;
    @SerializedName("firstadress")
    @Expose
    private String firstadress;
    @SerializedName("secondadress")
    @Expose
    private String secondadress;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("validation")
    @Expose
    private String verification;
    @SerializedName("vkAccessToken")
    @Expose
    private String vkAccessToken;

    @SerializedName("walker")
    @Expose
    private String walker;

    public String getWalker() {
        return walker;
    }

    public void setWalker(String walker) {
        this.walker = walker;
    }

    public String getVkAccessToken() {
        return vkAccessToken;
    }
    public void setVkAccessToken(String vkAccessToken) {
        this.vkAccessToken = vkAccessToken;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getVerification() {
        return verification;
    }
    public String getDriverver()
    {
        return driverver;

    }
    public void setDriverver(String s)
    {
        driverver = s;
    }
    public String getCompver()
    {
        return compver;
    }
    public void setCompver(String s)
    {
        compver = s;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getFirstadress(){
        return firstadress;
    }
    public void setFirstadress(String f) {
        firstadress = f;
    }
    public String getCountpass() {
        return countpass;
    }
    public void setCountpass(String c) {
        countpass = c;
    }
    public String getSecondadress(){
        return secondadress;
    }
    public void setSecondadress(String sc) {
        secondadress = sc;
    }
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver){
        this.driver = driver;
    }
    public String getCompanion() {
        return companion;
    }
    public void setCompanion(String companion){
        this.companion = companion;
    }
    public Boolean getReg() {
        return reg;
    }
    public void setReg(Boolean reg) {
        this.reg = reg;
    }
    public Boolean getLogin() {
        return login;
    }
    public void setLogin(Boolean login) {
        this.login = login;
    }
    public String getPicture() {
        return picture;
    }
    public String getDate() {
        return date;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSecondPassword() {
        return secondPassword;
    }
    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private final static long serialVersionUID = 7697500026652374235L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Data.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("reg");
        sb.append('=');
        sb.append(((this.reg == null)?"<null>":this.reg));
        sb.append(',');
        sb.append("login");
        sb.append('=');
        sb.append(((this.login == null)?"<null>":this.login));
        sb.append(',');
        sb.append("picture");
        sb.append('=');
        sb.append(((this.picture == null)?"<null>":this.picture));
        sb.append(',');
        sb.append("number");
        sb.append('=');
        sb.append(((this.number == null)?"<null>":this.number));
        sb.append(',');
        sb.append("mode");
        sb.append('=');
        sb.append(((this.mode == null)?"<null>":this.mode));
        sb.append(',');
        sb.append("text");
        sb.append('=');
        sb.append(((this.text == null)?"<null>":this.text));
        sb.append(',');
        sb.append("patronymic");
        sb.append('=');
        sb.append(((this.patronymic == null)?"<null>":this.patronymic));
        sb.append(',');
        sb.append("surname");
        sb.append('=');
        sb.append(((this.surname == null)?"<null>":this.surname));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("secondPassword");
        sb.append('=');
        sb.append(((this.secondPassword == null)?"<null>":this.secondPassword));
        sb.append(',');
        sb.append("password");
        sb.append('=');
        sb.append(((this.password == null)?"<null>":this.password));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}