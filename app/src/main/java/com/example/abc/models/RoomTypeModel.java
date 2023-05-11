package com.example.abc.models;

import java.io.Serializable;

public class RoomTypeModel implements Serializable {
    int numberRoomAvailable;
    String roomId, room, imageURL, description;
    int price;

    public RoomTypeModel() {
    }

    public RoomTypeModel(String roomId, String room, String imageURL, int price) {
        this.roomId = roomId;
        this.room = room;
        this.price = price;
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public int getNumberRoomAvailable() {
        return numberRoomAvailable;
    }

    public void setNumberRoomAvailable(int numberRoomAvailable) {
        this.numberRoomAvailable = numberRoomAvailable;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RoomTypeModel{" +
                "roomID=" + roomId +
                ", numberRoomAvailable=" + numberRoomAvailable +
                ", description='" + room + '\'' +
                ", price='" + price + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
