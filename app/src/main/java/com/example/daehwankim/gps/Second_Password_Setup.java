package com.example.daehwankim.gps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Daehwan.Kim on 2016-06-11.
 */
public class Second_Password_Setup extends ActionBarActivity {

    String myjson2;
    TextView setup;
    TextView edit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_password_setup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        setup = (TextView) findViewById(R.id.textView24);
        edit = (TextView) findViewById(R.id.textView25);

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myjson2.equals("1")){
                    //패스워드 설정된 상태
                    edit.setTextColor(Color.parseColor("#CCCCCC"));
                    setup.setText("2차 패스워드 설정");
                    edit.setText("패스워드 변경");
                    SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
                    password_delete("http://miraclehwan.vps.phps.kr/GPS/second_password_delete.php", idpref.getString("id",""));
                }else{
                    //패스워드 설정해야 하는 상태
                    Intent move_setup_page = new Intent(Second_Password_Setup.this, Second_Password_Setup_Page.class);
                    startActivity(move_setup_page);
                }
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
        password_check("http://miraclehwan.vps.phps.kr/GPS/second_password_check.php",idpref.getString("id", ""), "0");
    }

    public void password_check(String url, String id, String password){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];
                String password = params[2];

                BufferedReader bufferedReader = null;
                try{

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();



                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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

                if(myjson2.equals("1")){
                    edit.setTextColor(Color.parseColor("#000000"));
                    setup.setText("2차 패스워드 해제");
                    edit.setText("패스워드 변경");
                    Log.e("myjson2 LOG :" , myjson2);

                }else{
                    edit.setTextColor(Color.parseColor("#CCCCCC"));
                    setup.setText("2차 패스워드 설정");
                    edit.setText("패스워드 변경");
                    Log.e("myjson2 LOG :" , myjson2);

                }

            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id, password);

    }

    public void password_delete(String url, String id){
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



        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id);

    }

}
