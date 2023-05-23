package com.example.abc.models;

public class ReserveUserModel {
    private String userId;
    private String name;
    private String email;
    private String image;

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ReserveUserModel() {
    }

    public ReserveUserModel(String userId, String name, String email, String image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.image = image;
    }
}
