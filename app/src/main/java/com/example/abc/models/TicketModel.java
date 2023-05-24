package com.example.abc.models;

import java.io.Serializable;

public class TicketModel implements Serializable {
    int price, numberPerson;
    String userId, nameType, dateArrive, dateLeave, imageURL, ticketId, description, status, roomId, userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    long dateBooked;

    public long getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(long dateBooked) {
        this.dateBooked = dateBooked;
    }

    public String getDateLeave() {
        return dateLeave;
    }

    public void setDateLeave(String dateLeave) {
        this.dateLeave = dateLeave;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketModel() {
    }

    public String getTicketId() {
        return ticketId;
    }


    public TicketModel(String userId, String nameType, String dateArrive, String dateLeave, String imageURL,
                       int price, int numberPerson, String ticketId, String description,
                       String status, String roomId, long dateBooked, String userName) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.nameType = nameType;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
        this.imageURL = imageURL;
        this.price = price;
        this.numberPerson = numberPerson;
        this.description = description;
        this.status = status;
        this.roomId = roomId;
        this.dateBooked = dateBooked;
        this.userName = userName;
    }

    public TicketModel(String userId, String nameType, String dateArrive, String dateLeave, String imageURL,
                       int price, int numberPerson, String ticketId, String description,
                       String status, long dateBooked, String userName) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.nameType = nameType;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
        this.imageURL = imageURL;
        this.price = price;
        this.numberPerson = numberPerson;
        this.description = description;
        this.status = status;
        this.dateBooked = dateBooked;
        this.userName = userName;
    }

    public TicketModel(String nameType) {
        this.nameType = nameType;
    }

    public String getUserId() {
        return userId;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(String dateArrive) {
        this.dateArrive = dateArrive;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    @Override
    public String toString() {
        return "TicketModel{" +
                "userId='" + userId + '\'' +
                ", nameType='" + nameType + '\'' +
                ", time='" + dateArrive + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", numberPerson=" + numberPerson +
                '}';
    }
}
