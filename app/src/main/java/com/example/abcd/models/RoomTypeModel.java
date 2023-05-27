package com.example.abcd.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RoomTypeModel implements Serializable {
    String roomId, room, imageURL, description, roomType;
    int price;

    public RoomTypeModel() {
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public RoomTypeModel(String roomId, String room, String imageURL, String roomType, int price) {
        this.roomId = roomId;
        this.room = room;
        this.price = price;
        this.imageURL = imageURL;
        this.roomType = roomType;
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
                ", description='" + room + '\'' +
                ", price='" + price + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("price", price);
        result.put("room", room);
        result.put("roomType", roomType);
        result.put("imageURL", imageURL);
        return result;
    }
}
