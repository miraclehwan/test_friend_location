package com.example.daehwankim.gps;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Location extends ActionBarActivity {

    String myjson;
    String myjson2;
    JSONArray GET_info = null;
    JSONArray GET_info2 = null;
    private static final String TAG_RESULT = "result";
    private static final String TAG_RESULT2 = "result2";
    private static final String TAG_friend = "friend";
    private static final String TAG_time = "time";
    private static final String TAG_latitude = "latitude";
    private static final String TAG_longitude = "longitude";
    private static final String TAG_checked = "checked";
    private static final String TAG_friendname = "friendname";
    ArrayList<HashMap<String, String>> GET_info_List;
    ArrayList<HashMap<String, String>> GET_info_List2;
    ArrayList<String> data_friend = new ArrayList<String>();
    ArrayList<String> data_time = new ArrayList<String>();
    ArrayList<LatLng> data_latlng = new ArrayList<LatLng>();
    ArrayList<String> data_friendname = new ArrayList<String>();
    ArrayList<Boolean> data_checked = new ArrayList<Boolean>();



    GoogleMap map;
    GpsInfo gps;
    double latitude;
    double longitude;
    String address;
    SharedPreferences idpref;
    int count;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        count=0;



        idpref = getSharedPreferences("idpref", MODE_PRIVATE);

        getData("http://miraclehwan.vps.phps.kr/GPS/get_friend_location.php", idpref.getString("id", ""));
        setMap();

    }




    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.true_location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //친구목록 버튼
        int id = item.getItemId();
        if (id == R.id.action_button2) {
            FriendListDialog();
            return true;
        }

        if (id == R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMap(){// 맵, 주소 셋팅
        gps = new GpsInfo(Location.this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        final LatLng userlocation = new LatLng(latitude, longitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.setMyLocationEnabled(true);  // 오른쪽 위 내 위치 표시, 이동시에는 v 표시

        if(count==0){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 16));
            map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
            count = count + 1;
        }







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
            Toast.makeText(Location.this, "주소실패", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return bf.toString();
    }

    private void friendmarker(){
        MarkerOptions markerOptions = new MarkerOptions();

        for(int i=0; i<data_friend.size(); i++){
            markerOptions.position(data_latlng.get(i)).title(data_friend.get(i));
            markerOptions.snippet(findAddress(data_latlng.get(i).latitude, data_latlng.get(i).longitude));
            map.addMarker(markerOptions).showInfoWindow();
        }

        getFriendData("http://miraclehwan.vps.phps.kr/GPS/get_friend_list.php", idpref.getString("id", ""));

    }

    public void getFriendData(final String url, final String id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];


                BufferedReader bufferedReader = null;
                try {

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myjson2 = s;
                showFriendlist();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id);
    }

    protected void showFriendlist(){

        try {
            JSONObject jsonObject = new JSONObject(myjson2);
            GET_info2 = jsonObject.getJSONArray(TAG_RESULT2);

            for ( int i=0 ; i < GET_info2.length() ; i++){
                JSONObject c = GET_info2.getJSONObject(i);
                String json_friend = c.getString(TAG_friendname);
                String json_checked = c.getString(TAG_checked);

                data_friendname.add(i, json_friend);
                data_checked.add(i, Boolean.valueOf(json_checked));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendLocationData(final String url, final String id, final String friend, final String checked) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];
                String friend = params[2];
                String checked = params[3];

                BufferedReader bufferedReader = null;
                try {

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("friend", "UTF-8") + "=" + URLEncoder.encode(friend, "UTF-8");
                    data += "&" + URLEncoder.encode("checked", "UTF-8") + "=" + URLEncoder.encode(checked, "UTF-8");

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id, friend, checked);
    }

    public void getData(final String url, final String id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
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



            for ( int i=0 ; i < GET_info.length() ; i++){
                JSONObject c = GET_info.getJSONObject(i);
                String json_friend = c.getString(TAG_friend);
                String json_time = c.getString(TAG_time);
                String json_latitude = c.getString(TAG_latitude);
                String json_longitude = c.getString(TAG_longitude);



                data_friend.add(i, json_friend);
                data_time.add(i, json_time);
                data_latlng.add(i, new LatLng(Double.parseDouble(json_latitude), Double.parseDouble(json_longitude)));

                Log.e("ShowList Log :", data_friend.get(i));

            }
            friendmarker();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FriendListDialog(){
        final ArrayList<Location_Friend_ListView_Data > datas = new ArrayList<Location_Friend_ListView_Data>();
        Location_Friend_ListView_BaseAdapter adapter = new Location_Friend_ListView_BaseAdapter(getLayoutInflater(), datas);
        AlertDialog.Builder builder = new AlertDialog.Builder(Location.this);
        for(int i=0; i<data_friendname.size(); i++){
            datas.add(new Location_Friend_ListView_Data(data_friendname.get(i), data_checked.get(i)));
        }
        builder.setTitle("위치공유");
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });

        boolean[] checkedarray= new boolean[data_checked.size()];

        for(int i=0; i<data_checked.size(); i++){
            checkedarray[i] = data_checked.get(i);
        }
        boolean[] a = {true, true, true, false};
        builder.setMultiChoiceItems(data_friendname.toArray(new CharSequence[data_friendname.size()]), checkedarray, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    Log.e("CheckBox Log : ", String.valueOf(which) + isChecked);
                    datas.get(which).setCheck(true);
                    data_checked.set(which, true);
                }else if(!isChecked) {
                    Log.e("CheckBox Log : ", String.valueOf(which) + isChecked);
                    datas.get(which).setCheck(false);
                    data_checked.set(which, false);

                }
            }
        });

        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0; i<datas.size(); i++){
                    Log.e("save button log : ", datas.get(i).getName() + String.valueOf(datas.get(i).getCheck()));
                    sendLocationData("http://miraclehwan.vps.phps.kr/GPS/sen_friend_view_check.php", idpref.getString("id", ""),
                            datas.get(i).getName(), String.valueOf(datas.get(i).getCheck()));
                }
                Toast.makeText(Location.this, "저장완료", Toast.LENGTH_SHORT).show();
                data_friend.clear();
                data_time.clear();
                data_latlng.clear();
                data_friendname.clear();
                datas.clear();
                map.clear();
                getData("http://miraclehwan.vps.phps.kr/GPS/get_friend_location.php", idpref.getString("id", ""));
                setMap();
            }
        });


        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
