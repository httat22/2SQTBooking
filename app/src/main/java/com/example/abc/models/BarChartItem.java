package com.example.abc.models;

public class BarChartItem implements Comparable<BarChartItem>{
    int height;
    String roomId;

    public BarChartItem(int height, String roomId) {
        this.height = height;
        this.roomId = roomId;
    }
    public BarChartItem() {
        this.height = 0;
        this.roomId = "000";
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public int compareTo(BarChartItem barChartItem) {
        return barChartItem.getHeight() - this.height;
    }
}
