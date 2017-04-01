package com.example.daehwankim.gps;

/**
 * Created by Daehwan.Kim on 2016-06-04.
 */
public class Friend_Request_ListView_Data {


    String id;
    String friendid;
    String time;

    public Friend_Request_ListView_Data(String id, String friendid, String  time){
        this.id = id;
        this.friendid = friendid;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
