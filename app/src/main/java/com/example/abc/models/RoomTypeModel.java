package com.example.abc.models;

import java.io.Serializable;

public class RoomTypeModel implements Serializable {
    int id, numberRoomAvailable;
    String description, price, imageURL;

    public RoomTypeModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public RoomTypeModel(int id, String description, String imageURL, String price) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "RoomTypeModel{" +
                "id=" + id +
                ", numberRoomAvailable=" + numberRoomAvailable +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
