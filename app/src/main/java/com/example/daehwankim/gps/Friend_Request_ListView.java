package com.example.daehwankim.gps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class Friend_Request_ListView extends ActionBarActivity {

    ArrayList<Friend_Request_ListView_Data> datas = new ArrayList<Friend_Request_ListView_Data>();
    Friend_Request_ListView_BaseAdapter adapter;
    ListView listView;

    private static final String TAG_RESULT = "result";
    private static final String TAG_id = "id";
    private static final String TAG_friendid = "friendid";
    private static final String TAG_time = "time";
    String myjson;
    JSONArray GET_info = null;

    ArrayList<String> data_id = new ArrayList<String>();
    ArrayList<String> data_friendid = new ArrayList<String>();
    ArrayList<String> data_time = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView3);

        SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);

        getData("http://miraclehwan.vps.phps.kr/GPS/friend_list_request.php",idpref.getString("id", ""));

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.friend_add_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_button4) {
            Intent move = new Intent(Friend_Request_ListView.this, Friend_Add.class);
            startActivity(move);
            return true;
        }

        if (item.getItemId() == R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    };


    public void getData(String url, String id){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];

                BufferedReader bufferedReader = null;
                try{

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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

                myjson = s;
                showlist();
            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id);
    }


    protected void showlist(){

        try {
            JSONObject jsonObject = new JSONObject(myjson);
            GET_info = jsonObject.getJSONArray(TAG_RESULT);


            int j=0;
            for ( int i=0 ; i < GET_info.length() ; i++){
                JSONObject c = GET_info.getJSONObject(i);
                String json_id = c.getString(TAG_id);
                String json_friendid = c.getString(TAG_friendid);
                String json_time = c.getString(TAG_time);

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
                String today = format.format(date);


                try {
                    Date begin = format.parse(json_time);
                    Date end = format.parse(today);
                    long diff = end.getTime() - begin.getTime();
                    long diffDays = diff / (60 * 60 * 1000);


                    if(diffDays < 24){
                        data_id.add(j, json_id);
                        data_friendid.add(j, json_friendid);
                        data_time.add(j, json_time);

                        datas.add(j, new Friend_Request_ListView_Data(json_id, json_friendid, json_time));

                        Log.e("id log : " , json_id );
                        Log.e("friendid log : " , json_friendid );
                        Log.e("time log : " , json_time );
                        j++;
                    }else{
                        Log.e("id log : " , json_id );
                        Log.e("friendid log : " , json_friendid );
                        deleteData("http://miraclehwan.vps.phps.kr/GPS/friend_list_delete.php", json_id, json_friendid);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }





            }



            adapter = new Friend_Request_ListView_BaseAdapter(getLayoutInflater(), datas);

            listView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void deleteData(String url, String id, String friendid){
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
