package com.example.daehwankim.gps;

/**
 * Created by Daehwan.Kim on 2016-05-23.
 */
public class Location_Friend_ListView_Data {

    String name;
    Boolean check;

    public Location_Friend_ListView_Data(String name, Boolean check){
        this.name = name;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
