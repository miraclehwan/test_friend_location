package com.example.daehwankim.gps;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daehwan.Kim on 2016-06-04.
 */
public class Friend_Request_ListView_BaseAdapter extends BaseAdapter {

    ArrayList<Friend_Request_ListView_Data> datas;
    LayoutInflater inflater;

    public Friend_Request_ListView_BaseAdapter(LayoutInflater inflater, ArrayList<Friend_Request_ListView_Data> datas){
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.friend_request_list_item, null);
        }

        TextView friendid = (TextView) convertView.findViewById(R.id.textView22);
        TextView time = (TextView) convertView.findViewById(R.id.textView23);
        Button OKbutton = (Button) convertView.findViewById(R.id.button10);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        String today = format.format(date);

        try {
            Date begin = format.parse(datas.get(position).getTime());
            Date end = format.parse(today);
            long diff = end.getTime() - begin.getTime();
            long diffDays = diff / (60 * 60 * 1000);

            friendid.setText(datas.get(position).getId());
            time.setText((24-diffDays) + "시간 남음");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        OKbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData("http://miraclehwan.vps.phps.kr/GPS/friend_button_request.php", datas.get(position).getId(),datas.get(position).getFriendid());
                datas.remove(position);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }



    public void getData(String url, String id, String friendid){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];
                String friendid = params[2];

                BufferedReader bufferedReader = null;
                try{

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("friendid", "UTF-8") + "=" + URLEncoder.encode(friendid, "UTF-8");


                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write( data );
                    wr.flush();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine()) != null){
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id, friendid);
    }

}
