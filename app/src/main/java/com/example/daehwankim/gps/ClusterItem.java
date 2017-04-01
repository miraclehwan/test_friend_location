package com.example.daehwankim.gps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daehwan.Kim on 2016-05-21.
 */
public class ClusterItem implements com.google.maps.android.clustering.ClusterItem {

    private LatLng location;
    private String address;
    private String time;
    public ClusterItem(LatLng latlng, String time, String address){
        this.location = latlng;
        this.address = address;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public LatLng getPosition() {
        DailyLocation.ClusterItem_set_time = getTime();
        return location;
    }


}
