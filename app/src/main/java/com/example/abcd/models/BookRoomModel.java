package com.example.abcd.models;

import java.io.Serializable;

public class BookRoomModel implements Serializable {
    int totalPayment;
    int price;
    String userId, roomId;
    String dateArrive, dateLeave;

    public int getTotalPayment() {
        return totalPayment;
    }

    public BookRoomModel() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public BookRoomModel(int totalPayment, int price, String userId, String roomId, String dateArrive, String dateLeave) {
        this.totalPayment = totalPayment;
        this.price = price;
        this.userId = userId;
        this.roomId = roomId;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
    }

    @Override
    public String toString() {
        return "BookRoomModel{" +
                "totalPayment=" + totalPayment +
                ", price=" + price +
                ", roomId=" + roomId +
                ", userId='" + userId + '\'' +
                ", dateArrive='" + dateArrive + '\'' +
                ", dateLeave='" + dateLeave + '\'' +
                '}';
    }

    public BookRoomModel(int price, String userId, String roomId, String dateArrive, String dateLeave) {
        this.price = price;
        this.userId = userId;
        this.roomId = roomId;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
    }
}
