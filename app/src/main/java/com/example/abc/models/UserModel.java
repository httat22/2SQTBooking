package com.example.abc.models;

public class UserModel {
    String userId, userName, email, phone, address;

    public UserModel() {}

    public String getUserId() {
        return userId;
    }
    public UserModel(String userId, String userName, String email, String phone, String address) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
