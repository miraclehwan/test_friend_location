package com.example.daehwankim.gps;

import android.content.Context;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Daehwan.Kim on 2016-05-23.
 */
public class Location_Friend_ListView_BaseAdapter extends BaseAdapter {

    ArrayList<Location_Friend_ListView_Data> datas;
    LayoutInflater inflater;

    public Location_Friend_ListView_BaseAdapter(LayoutInflater inflater, ArrayList<Location_Friend_ListView_Data> datas){
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = inflater.inflate(R.layout.locatioin_friend_list_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.textView19);
        final CheckBox checked = (CheckBox) convertView.findViewById(R.id.checkBox2);

        name.setText(datas.get(position).getName());
        checked.setChecked(datas.get(position).getCheck());

//        checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    Log.e("SaveToncheckedchanged :", String.valueOf(isChecked));
//                    Location.data_checked.set(position, isChecked);
//                }else if(!isChecked){
//                    Log.e("SaveFoncheckedchanged :", String.valueOf(isChecked));
//                    Location.data_checked.set(position, isChecked);
//                }
//            }
//        });

//        checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Log.e("checktrue : ", datas.get(position).getName());
//                    Location.data_checked.add(position, true);
//                    notifyDataSetChanged();
//                }else{
//                    Log.e("checkfalse : ", datas.get(position).getName());
//                    Location.data_checked.add(position, false);
//                    notifyDataSetChanged();
//                }
//            }
//        });

        return convertView;
    }


}
