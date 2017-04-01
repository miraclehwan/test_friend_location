package com.example.daehwankim.gps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daehwan.Kim on 2016-04-30.
 */
public class GPSintentService extends Service implements Runnable {

    private static final int REBOOT_DELAY_TIMER = 10 * 1000;

    private static final int LOCATION_UPDATE_DELAY = 600 * 1000;

    private Handler mHandler;
    private boolean mIsRunning;
    private int mStartId = 0;
    public   static int tempa=0;




    Date date;
    SimpleDateFormat day;
    String time;



    public void onCreate(){
        unregisterRestartAlarm();
        super.onCreate();
        mIsRunning = false;
    }

    public void onDestory(){
        registerRestartAlarm();
        super.onDestroy();
        mIsRunning = false;
    }

    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);

        mHandler = new Handler();
        mHandler.postDelayed(this, LOCATION_UPDATE_DELAY);
        mIsRunning = true;
    }


    @Override
    public void run() {
        Log.e("RUN LOG", "run()");

        if(!mIsRunning){

            Log.d("GPSintentService Log", "mIsRunning is false");

            return ;
        } else {
            //서버로 정보 보내는 부분
            SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
            SharedPreferences.Editor ideditor = idpref.edit();


            date = new Date();
            day = new SimpleDateFormat("yyyyMMddHHmm");
            time = day.format(date);

            final GpsInfo gps = new GpsInfo(getApplication());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String get_id = idpref.getString("id","");
            String get_time = time;
            String get_latitude = Double.toString(latitude);
            String get_longitude = Double.toString(longitude);
            //insertToDatabase(get_id, get_time, get_latitude, get_longitude);
            //여기까지임 - 서버정보보내기
            InsertData task = new InsertData();
            task.execute(get_id, get_time, get_latitude, get_longitude);
            Log.e("GPSintentService Log", "id=" + get_id + " / time=" + get_time + " / latitude=" + get_latitude + " / longitude=" + get_longitude);
            Log.e("GPSintentService Log", "mIsRunning is true");

            mHandler.postDelayed(this, LOCATION_UPDATE_DELAY);
            mIsRunning = true;
        }
    }

    private void registerRestartAlarm(){
        Intent intent = new Intent(GPSintentService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(GPSintentService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += REBOOT_DELAY_TIMER;

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, REBOOT_DELAY_TIMER, sender);
    }

    private void unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()");
        Intent intent = new Intent(GPSintentService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(GPSintentService.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    //Location send(view가 없어서 아래 함수는 사용 못함.. 사용법 찾아보기.(일단 임시로 다 선언해서 Asynctask 호출하여 사용)
    public void Location_send(View view){
        SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
        SharedPreferences.Editor ideditor = idpref.edit();



        //String get_id = idpref.getString("id","");
        //String get_time = time;
        //String get_latitude = String.valueOf(latitude);
        //String get_longitude = String.valueOf(longitude);


        //insertToDatabase(get_id, get_time, get_latitude, get_longitude);

    }

    //private void insertToDatabase(String id, String time, String latitude, String longitude){

        private class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override // 결과출력
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("AsyntackLog :", s);

            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String time = (String)params[1];
                    String latitude = (String)params[2];
                    String longitude = (String)params[3];

                    String link="http://115.71.238.147/GPS/location_sen_query.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
                    data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        //InsertData task = new InsertData();
        //task.execute(id, time, latitude, longitude);
    //}




}























