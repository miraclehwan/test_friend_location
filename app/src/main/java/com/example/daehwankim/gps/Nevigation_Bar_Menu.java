package com.example.daehwankim.gps;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daehwan.Kim on 2016-06-18.
 */
public class Nevigation_Bar_Menu extends ActionBarActivity {

    GoogleMap map;
    GpsInfo gps;
    double latitude;
    double longitude;

    Intent GPS_Service;
    BroadcastReceiver receiver;

    ActionBarDrawerToggle drawerToggle;
    String [] drawer_str={"일별위치조회", "친구위치조회", "친구관리", "2차패스워드설정"};

    String tempaddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation_menu);

        registBroadcastReceiver();
        getInstanceIdToken();

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        final ListView listView = (ListView) findViewById(R.id.drawer);

        gps = new GpsInfo(Nevigation_Bar_Menu.this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        final LatLng userlocation = new LatLng(latitude, longitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.nevi_map)).getMap();

        map.setMyLocationEnabled(true);  // 오른쪽 위 내 위치 표시, 이동시에는 v 표시

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 16));
        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

        TextView address = (TextView) findViewById(R.id.textView28);
        address.setText(findAddress(latitude, longitude));



        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,drawer_str);
        listView.setAdapter(adapter);

        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);


//        getSupportActionBar().setHomeButtonEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent movedailylocation = new Intent(Nevigation_Bar_Menu.this, DailyLocation.class);
                        startActivity(movedailylocation);
                        break;
                    case 1:
                        Intent movelocation = new Intent(Nevigation_Bar_Menu.this, Location.class);
                        startActivity(movelocation);
                        break;
                    case 2:
                        Intent move_add_friend = new Intent(Nevigation_Bar_Menu.this, Friend_Request_ListView.class);
                        startActivity(move_add_friend);
                        break;
                    case 3:
                        Intent second_password_move = new Intent(Nevigation_Bar_Menu.this, Second_Password_Setup.class);
                        startActivity(second_password_move);
                        break;
                }
                drawerLayout.closeDrawer(listView);
            }
        });


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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        return true;
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //메뉴버튼
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        //주소전달버튼
        int id = item.getItemId();
        if (id == R.id.action_button) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Nevigation_Bar_Menu.this);

            TextView message = new TextView(this);
            message.setText("원하는 작업을 선택 해주세요.");
            message.setGravity(Gravity.CENTER);


            ab.setMessage(message.getText());
            ab.setPositiveButton("전달", Output_Click_Listener);
            ab.setNegativeButton("복사", Copy_Click_Listener);
            ab.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private DialogInterface.OnClickListener Copy_Click_Listener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            ClipboardManager addressclipmanager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData addressclipdata = ClipData.newPlainText("label", tempaddress);
            addressclipmanager.setPrimaryClip(addressclipdata);
            Toast.makeText(Nevigation_Bar_Menu.this, "주소가 복사 되었습니다.", Toast.LENGTH_SHORT).show();

        }
    };

    private DialogInterface.OnClickListener Output_Click_Listener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

//            Intent capture = new Intent(Location.this, Temp_Location_Capture.class);
//            capture.putExtra("lat",latitude);
//            capture.putExtra("long",longitude);
//
//            startActivity(capture);


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("sms_body", tempaddress);
            intent.setType("vnd.android-dir/mms-sms");
            startActivity(intent);

        }
    };






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


    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAdminArea()
                            + ' ' + address.get(0).getLocality()
                            + ' ' + address.get(0).getThoroughfare()
                            + ' ' + address.get(0).getFeatureName();
                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                }
            }

        } catch (IOException e) {
            Toast.makeText(Nevigation_Bar_Menu.this, "주소변환실패", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        tempaddress=bf.toString();
        return bf.toString();
    }



}