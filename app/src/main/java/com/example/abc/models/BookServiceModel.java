package com.example.abc.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BookServiceModel implements Serializable {
    int totalPayment;
    int price, numberPerson;
    String userId;
    String dateArrive, dateLeave;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BookServiceModel() {
    }
    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberPerson() {
        return numberPerson;
    }

    public void setNumberPerson(int numberPerson) {
        this.numberPerson = numberPerson;
    }

    public String getUserId() {
        return userId;
    }

    public String getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(String dateArrive) {
        this.dateArrive = dateArrive;
    }

    public String getDateLeave() {
        return dateLeave;
    }

    public void setDateLeave(String dateLeave) {
        this.dateLeave = dateLeave;
    }

    public BookServiceModel(int price, int numberPerson, String userId, String dateArrive, String dateLeave) {
        this.price = price;
        this.numberPerson = numberPerson;
        this.userId = userId;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
    }
}
