package com.example.daehwankim.gps;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daehwan.Kim on 2016-06-04.
 */
public class Friend_Add extends ActionBarActivity {

    EditText friend_id;
    Button add_button;
    String myjson;
    String myjson2;
    String time;
    SharedPreferences idpref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        friend_id =  (EditText) findViewById(R.id.editText11);
        add_button = (Button) findViewById(R.id.button9);
        idpref = getSharedPreferences("idpref", MODE_PRIVATE);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( friend_id.getText().length() > 0 ) {
                    if( friend_id.getText().toString().equals(idpref.getString("id", "").toString() )){
                        Toast.makeText(Friend_Add.this, "사용자의 ID와 같습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        getData("http://miraclehwan.vps.phps.kr/GPS/friend_check.php", idpref.getString("id", ""), friend_id.getText().toString());
                    }
                }else{
                    Toast.makeText(Friend_Add.this, "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    };

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

                    Date date = new Date();
                    SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHH");
                    time = day.format(date);

                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("friendid", "UTF-8") + "=" + URLEncoder.encode(friendid, "UTF-8");
                    data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");

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
                Log.e("myjson : ", s);
                if(myjson.equals("duplicate")){
                    Toast.makeText(Friend_Add.this, "이미 친구인 사용자ID 입니다.", Toast.LENGTH_SHORT).show();
                }else if(Integer.valueOf(myjson) > 0){
                    friend_add("http://miraclehwan.vps.phps.kr/GPS/friend_add.php", idpref.getString("id", ""), friend_id.getText().toString(), time);
                }else{
                    Toast.makeText(Friend_Add.this, "존재하지 않는 사용자ID 입니다.", Toast.LENGTH_SHORT).show();
                }

            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id, friendid);

    }


    public void friend_add(String url, String id, String friendid, String time){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];
                String friendid = params[2];
                String time = params[3];

                BufferedReader bufferedReader = null;
                try{

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();



                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("friendid", "UTF-8") + "=" + URLEncoder.encode(friendid, "UTF-8");
                    data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");

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
                myjson2 = s;

                if(myjson2.equals("insert_OK")){
                    Log.e("myjson2 LOG :" , myjson2);
                    Toast.makeText(Friend_Add.this, "친구요청 완료", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("myjson2 LOG :" , myjson2);
                    Toast.makeText(Friend_Add.this, "이미 친구요청이 진행중인 ID 입니다.", Toast.LENGTH_SHORT).show();
                }

            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id, friendid, time);

    }




}
