package com.example.abc.models;

public class RoomModel {
    int image;
    String description, price;
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public RoomModel(int image, String description, String price) {
        this.image = image;
        this.description = description;
        this.price = price;
    }
}
