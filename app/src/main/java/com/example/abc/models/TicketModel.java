package com.example.abc.models;

public class TicketModel {
    String userId, nameType, time, imageURL, ticketId, description, status;

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

    int price, numberPerson;

    public TicketModel() {}

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public TicketModel(String userId, String nameType, String time, String imageURL,
                       int price, int numberPerson, String ticketId, String description, String status) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.nameType = nameType;
        this.time = time;
        this.imageURL = imageURL;
        this.price = price;
        this.numberPerson = numberPerson;
        this.description = description;
        this.status = status;
    }

    public TicketModel(String nameType) {
        this.nameType = nameType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
                ", time='" + time + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", numberPerson=" + numberPerson +
                '}';
    }
}
