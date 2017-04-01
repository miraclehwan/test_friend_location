package com.example.daehwankim.gps;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.ServiceState;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daehwan.Kim on 2016-04-16.
 */
public class Menu extends FragmentActivity {

    Intent GPS_Service;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        registBroadcastReceiver();
        getInstanceIdToken();

        ImageView location = (ImageView) findViewById(R.id.imageView14);
        TextView day = (TextView) findViewById(R.id.textView4);
        TextView year = (TextView) findViewById(R.id.textView5);
        TextView friend_request = (TextView) findViewById(R.id.textView20);
        TextView friend_list = (TextView) findViewById(R.id.textView21);
        final TextView second_password = (TextView) findViewById(R.id.textView26);

        TextView nevigation = (TextView) findViewById(R.id.textView29);


        Date date = new Date();
        SimpleDateFormat dayformat = new SimpleDateFormat("d");
        SimpleDateFormat yearformat = new SimpleDateFormat("LLLL");
        year.setText(yearformat.format(date));
        day.setText(dayformat.format(date));


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movelocation = new Intent(Menu.this, Location.class);
                startActivity(movelocation);

            }
        });

        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movedailylocation = new Intent(Menu.this, DailyLocation.class);
                startActivity(movedailylocation);
            }
        });


        friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_add_friend = new Intent(Menu.this, Friend_Add.class);
                startActivity(move_add_friend);
            }
        });

        friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_friend_list = new Intent(Menu.this, Friend_Request_ListView.class);
                startActivity(move_friend_list);
            }
        });

        second_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent second_password_move = new Intent(Menu.this, Second_Password_Setup.class);
                startActivity(second_password_move);
            }
        });

        nevigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nevigation_move = new Intent(Menu.this, Nevigation_Bar_Menu.class);
                startActivity(nevigation_move);
            }
        });

        //GPS Service 구현

        GPS_Service = new Intent(this, GPSintentService.class);
        receiver = new RestartService();

        try {
            IntentFilter mainfilter = new IntentFilter("com.example.daehwankim.gps.RestartService@17d340a1");
            registerReceiver(receiver, mainfilter);

            if (isServiceRunningCheck() == false) {
                startService(GPS_Service);
                Log.d("Menu Log", "Service 실행");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnDestory() {
        //unregisterReceiver(receiver);
        super.onDestroy();
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.daehwankim.gps.GPSintentService".equals(service.service.getClassName())) {
                Log.d("isServiceRunning Log", service.service.getClassName().toString());
                return true;
            }
        }
        return false;
    }







    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Menu";

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if (action.equals(QuickstartPreferences.REGISTRATION_READY)) {
                    // 액션이 READY일 경우
                } else if (action.equals(QuickstartPreferences.REGISTRATION_GENERATING)) {
                    // 액션이 GENERATING일 경우

                } else if (action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)) {
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    Log.e("token Log :", token);
                    SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
                    send_gcm_key(idpref.getString("id", ""), token);
                }

            }
        };
    }


    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private void send_gcm_key(final String id, String keyvalue){

        class checkData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {



                try{
                    String id = (String)params[0];
                    String keyvalue = (String)params[1];
                    Log.e("doIn Log : ", id);
                    Log.e("doIn Log : ", keyvalue);
                    String link="http://115.71.238.147/GPS/gcm_key_verify.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("keyvalue", "UTF-8") + "=" + URLEncoder.encode(keyvalue, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();


                    InputStreamReader temp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(temp);
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // php echo값 가져오기

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

        checkData task = new checkData();
        task.execute(id, keyvalue);
    }


}



