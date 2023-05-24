package com.example.abc.models;

public class UserModel {
    private String userId, userName, email, phone, address, salt, imageURL;
    private boolean isLogin;
    private int loginAttempts;
    private long lastFailedLoginTime;
    public UserModel() {
    }

    public UserModel(String userId, String userName, String email, String phone, String address, String salt) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.salt = salt;
        this.loginAttempts = 0;
        this.lastFailedLoginTime = 0;
        this.isLogin = false;
        this.imageURL = "";
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public long getLastFailedLoginTime() {
        return lastFailedLoginTime;
    }

    public void setLastFailedLoginTime(long lastFailedLoginTime) {
        this.lastFailedLoginTime = lastFailedLoginTime;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
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
