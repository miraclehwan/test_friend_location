package com.example.daehwankim.gps;

/**
 * Created by Daehwan.Kim on 2016-05-15.
 */
public class DailyLocation_ListView_Data {

    String time;
    String address;

    public DailyLocation_ListView_Data(String time, String address){

        this.time = time;
        this.address = address;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
