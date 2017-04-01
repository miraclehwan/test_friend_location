package com.example.daehwankim.gps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daehwan.Kim on 2016-05-15.
 */
public class DailyLocation_ListView_BaseAdapter extends BaseAdapter {

    ArrayList<DailyLocation_ListView_Data> datas;
    LayoutInflater inflater;

    public DailyLocation_ListView_BaseAdapter(LayoutInflater inflater, ArrayList<DailyLocation_ListView_Data> datas){
        this.datas = datas;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.dailylocation_listview_item, null);
        }

        TextView time = (TextView) convertView.findViewById(R.id.textView10);
        TextView address = (TextView) convertView.findViewById(R.id.textView12);

        time.setText(datas.get(position).getTime());
        address.setText(datas.get(position).getAddress());


        return convertView;
    }
}
