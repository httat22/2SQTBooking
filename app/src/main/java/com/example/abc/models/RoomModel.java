package com.example.abc.models;

public class RoomModel {
    int id;



    String description, price, imageURL;

    public RoomModel() {
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

    public RoomModel(int id, String description, String imageURL, String price) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
    }
}
