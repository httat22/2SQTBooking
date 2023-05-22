package com.example.abc.models;

public class InfoRoomBookedModel {
    String roomId;
    String dateArrive;
    String dateLeave;
    long dateBooked;
    String userId;
    int numberPerson;

    public InfoRoomBookedModel() {
    }

    public InfoRoomBookedModel(String roomId, String dateArrive, String dateLeave, int numberPerson, long dateBooked, String userId) {
        this.roomId = roomId;
        this.dateArrive = dateArrive;
        this.dateLeave = dateLeave;
        this.numberPerson = numberPerson;
        this.dateBooked = dateBooked;
        this.userId = userId;
    }

    public long getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(long dateBooked) {
        this.dateBooked = dateBooked;
    }

    public String getUserId() {
        return userId;
    }

    public int getNumberPerson() {
        return numberPerson;
    }

    public void setNumberPerson(int numberPerson) {
        this.numberPerson = numberPerson;
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
}
