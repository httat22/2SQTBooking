package com.example.abcd.models;

public class NotificationModel {
    private String notification, userId;
    long dateBooked;

    public NotificationModel(String notification, String userId, long dateBooked) {
        this.notification = notification;
        this.userId = userId;
        this.dateBooked = dateBooked;
    }

    public NotificationModel() {
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(long dateBooked) {
        this.dateBooked = dateBooked;
    }
}
