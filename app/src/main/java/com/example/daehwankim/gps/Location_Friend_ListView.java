package com.example.daehwankim.gps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Daehwan.Kim on 2016-05-23.
 */
public class Location_Friend_ListView extends ActionBarActivity {

    ArrayList<Location_Friend_ListView_Data> datas = new ArrayList<Location_Friend_ListView_Data>();
    ListView listView;
    Location_Friend_ListView_BaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_friend_list);

        listView = (ListView) findViewById(R.id.listView2);

        datas.add(new Location_Friend_ListView_Data("Daehwan", Boolean.TRUE));
        datas.add(new Location_Friend_ListView_Data("Sujin", Boolean.FALSE));

        adapter = new Location_Friend_ListView_BaseAdapter(getLayoutInflater(), datas);



        listView.setAdapter(adapter);


    }


}
