package com.example.abc.models;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return "BookServiceModel{" +
                "totalPayment=" + totalPayment +
                ", price=" + price +
                ", numberPerson=" + numberPerson +
                ", userId='" + userId + '\'' +
                ", dateArrive='" + dateArrive + '\'' +
                ", dateLeave='" + dateLeave + '\'' +
                '}';
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
        this.price = 25;
        this.numberPerson = numberPerson;
        this.userId = userId;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
    }
}
