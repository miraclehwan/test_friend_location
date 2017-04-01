package com.example.daehwankim.gps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daehwan.Kim on 2016-04-23.
 */
public class DailyLocation extends ActionBarActivity {

    ProgressDialog dialog;

    public static String ClusterItem_set_time;

    String myjson;

    private static final String TAG_RESULT = "result";
    private static final String TAG_time = "time";
    private static final String TAG_latitude = "latitude";
    private static final String TAG_longitude = "longitude";

    JSONArray GET_info = null;

    ArrayList<HashMap<String, String>> GET_info_List;

    ArrayList<String> data_time = new ArrayList<String>();
    ArrayList<LatLng> data_latlng = new ArrayList<LatLng>();

    String time;
    String time1;
    String time2;

    ClusterManager<MyItem> mClusterManager;

    GoogleMap map;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailylocation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        final GpsInfo gps = new GpsInfo(DailyLocation.this);
//        double latitude = gps.getLatitude();
//        double longitude = gps.getLongitude();
//        final LatLng userlocation = new LatLng(latitude, longitude);
//
//        TextView distance = (TextView) findViewById(R.id.textView6);
//        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//
//
//



        SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
        SharedPreferences.Editor ideditor = idpref.edit();

        Date date = new Date();
        SimpleDateFormat tempday = new SimpleDateFormat("yyyyMMdd");
        time = tempday.format(date);
        time1 = time + "0000";
        time2 = time + "5959";
        Log.e("DailyLocation Log(time)", time1);
        Log.e("DailyLocation Log(time)", time2);
        GET_info_List = new ArrayList<HashMap<String, String>>();
        getData("http://miraclehwan.vps.phps.kr/GPS/get_daily_location.php", idpref.getString("id", ""), time1, time2);
//
//        // Marker me = map.addMarker(new MarkerOptions().position(userlocation).title("Me"));   빨간색 표지판으로 마크남기기
//        map.setMyLocationEnabled(true);  // 오른쪽 위 내 위치 표시, 이동시에는 v 표시
//
//        LatLng tempplace = new LatLng(37.576139, 126.976852);
//        LatLng tempplace2 = new LatLng(37.575833, 126.973301);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tempplace, 14));
//        PolylineOptions temppolylin = new PolylineOptions().add(new LatLng(37.576139, 126.976852))
//                                                            .add(new LatLng(37.575833, 126.973301))
//                                                            .color(Color.RED);
//        Polyline polyline = map.addPolyline(temppolylin);
//        double tempdistance = SphericalUtil.computeDistanceBetween(tempplace, tempplace2);  // 거리계산
//        distance.setText("총 이동거리 - " + (int) tempdistance + "m");
//        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(37.576139, 126.976852)).title("Start!!").snippet("This is start point!!");
//        map.addMarker(markerOptions).showInfoWindow();



    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.daily_location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_button3) {
            Intent move = new Intent(DailyLocation.this, DailyLocation_ListView.class);
            startActivity(move);
            return true;
        }

        if (item.getItemId() == R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    };

    protected void makemap(){

        final GpsInfo gps = new GpsInfo(DailyLocation.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        final LatLng userlocation = new LatLng(latitude, longitude);

        TextView distance = (TextView) findViewById(R.id.textView6);
//        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();



        // Marker me = map.addMarker(new MarkerOptions().position(userlocation).title("Me"));   빨간색 표지판으로 마크남기기
        map.setMyLocationEnabled(true);  // 오른쪽 위 내 위치 표시, 이동시에는 v 표시


        //폴리라인 관련
//        PolylineOptions temppolylin = new PolylineOptions().add(new LatLng(37.576139, 126.976852))
//                .add(new LatLng(37.575833, 126.973301))
//                .color(Color.RED);





        //폴리라인선언
        PolylineOptions testpolyline = new PolylineOptions().color(Color.GREEN);

        //표시할 마커, 거리계산, 폴리라인 기준점 최초 1회 생성, 그후 비교하여 50m 이상거리만 표시
        MarkerOptions markerOptions = new MarkerOptions();

        String temptitle = data_time.get(0).substring(data_time.get(0).length()-4, data_time.get(0).length());
        markerOptions.position(data_latlng.get(0)).title(temptitle.substring(0,2) + ":" + temptitle.substring(2,4)).snippet(findAddress(data_latlng.get(0)));

        testpolyline.add(data_latlng.get(0));

        //맵에 마커표시
        map.addMarker(markerOptions).showInfoWindow();






        for (int i=1 ; i<data_latlng.size() ; i++)
        {
            if( 50 < (int) SphericalUtil.computeDistanceBetween(data_latlng.get(i-1), data_latlng.get(i))){
                temptitle = data_time.get(i).substring(data_time.get(i).length()-4, data_time.get(i).length());
                markerOptions.position(data_latlng.get(i)).title(temptitle.substring(0,2) + ":" + temptitle.substring(2,4)).snippet(findAddress(data_latlng.get(i)));
                //맵에 마커표시
                map.addMarker(markerOptions).showInfoWindow();
                testpolyline.add(data_latlng.get(i));
            }

        }

        //맵에 폴리라인 표시
        Polyline polyline = map.addPolyline(testpolyline);



        //거리계산
//        double tempdistance = SphericalUtil.computeDistanceBetween(tempplace, tempplace2);
        double testdistance = 0;
        for (int i=0; i<data_latlng.size()-1; i++){
            testdistance += SphericalUtil.computeDistanceBetween(data_latlng.get(i), data_latlng.get(i+1));
        }



        //거리계산 후 이동거리 표시
        if ( (int) testdistance < 1000){
            distance.setText("총 이동거리 - " + (int) testdistance + "m");
        }else{
            int tempdistance = (int)testdistance;
            String str = String.format("%,d", tempdistance);
            Log.e("Distance Log : ", str);
            str =str.replace(",", ".");
            Log.e("Distance Log : ", str);
            distance.setText("총 이동거리 - " + str + "km");
        }



        map.moveCamera(CameraUpdateFactory.newLatLngZoom(data_latlng.get(data_latlng.size()-1), 16));


    }



    protected void showlist(){

        try {
            JSONObject jsonObject = new JSONObject(myjson);
            GET_info = jsonObject.getJSONArray(TAG_RESULT);



            for ( int i=0 ; i < GET_info.length() ; i++){
                JSONObject c = GET_info.getJSONObject(i);
                String json_time = c.getString(TAG_time);
                String json_latitude = c.getString(TAG_latitude);
                String json_longitude = c.getString(TAG_longitude);

                data_time.add(i, json_time);
                data_latlng.add(i, new LatLng(Double.parseDouble(json_latitude), Double.parseDouble(json_longitude)));


                HashMap<String,String> json_data = new HashMap<String,String>();

                json_data.put(TAG_time, json_time);
                json_data.put(TAG_latitude, json_latitude);
                json_data.put(TAG_longitude, json_longitude);

                GET_info_List.add(json_data);
            }


            Log.e("JSON time Log : ", data_time.toString());
            Log.e("JSON latlng Log : ", data_latlng.toString());

//            makemap();
            onMapReady();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void onMapReady(){

        final GpsInfo gps = new GpsInfo(DailyLocation.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        final LatLng userlocation = new LatLng(latitude, longitude);

        TextView distance = (TextView) findViewById(R.id.textView6);
//        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(data_latlng.get(data_latlng.size()-1), 11));



        // Marker me = map.addMarker(new MarkerOptions().position(userlocation).title("Me"));   빨간색 표지판으로 마크남기기
        map.setMyLocationEnabled(true);  // 오른쪽 위 내 위치 표시, 이동시에는 v 표시





        ClusterManager<ClusterItem> mClusterManager = new ClusterManager<>(this, map);
        map.setOnCameraChangeListener(mClusterManager);

        for(int i=0; i<data_latlng.size(); i++){
            mClusterManager.addItem(new com.example.daehwankim.gps.ClusterItem(data_latlng.get(i), data_time.get(i), "ClusterItem"+1));

        }

        map.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterItem>() {
            @Override
            public boolean onClusterItemClick(ClusterItem clusterItem) {

                final View innerView = getLayoutInflater().inflate(R.layout.address_dialog, null);

                TextView time = (TextView) innerView.findViewById(R.id.textView17);
                TextView address = (TextView) innerView.findViewById(R.id.textView18);

                AlertDialog.Builder ab = new AlertDialog.Builder(DailyLocation.this);
                ab.setView(innerView);

                time.setText(ClusterItem_set_time.substring(8,10) + ":" + ClusterItem_set_time.substring(10,12));
                address.setText(findAddress(clusterItem.getPosition()));

                ab.show();

//                Toast toast = Toast.makeText(DailyLocation.this, ClusterItem_set_time.substring(8,10) + ":" + ClusterItem_set_time.substring(10,12) + "\n" + findAddress(clusterItem.getPosition()), Toast.LENGTH_SHORT);
//                toast.show();
//                Log.e("Cluster Click : ", "ok");
                return true;
            }
        });


        PolylineOptions testpolyline = new PolylineOptions().color(Color.GREEN);

        //표시할 마커, 거리계산, 폴리라인 기준점 최초 1회 생성, 그후 비교하여 50m 이상거리만 표시
        MarkerOptions markerOptions = new MarkerOptions();

        String temptitle = data_time.get(0).substring(data_time.get(0).length()-4, data_time.get(0).length());
        markerOptions.position(data_latlng.get(0)).title(temptitle.substring(0,2) + ":" + temptitle.substring(2,4)).snippet(findAddress(data_latlng.get(0)));

        testpolyline.add(data_latlng.get(0));

        //맵에 마커표시
//        map.addMarker(markerOptions).showInfoWindow();






        for (int i=1 ; i<data_latlng.size() ; i++)
        {
            if( 50 < (int) SphericalUtil.computeDistanceBetween(data_latlng.get(i-1), data_latlng.get(i))){
                temptitle = data_time.get(i).substring(data_time.get(i).length()-4, data_time.get(i).length());

                markerOptions.position(data_latlng.get(i)).title(temptitle.substring(0,2) + ":" + temptitle.substring(2,4)).snippet(findAddress(data_latlng.get(i)));
                //맵에 마커표시
//                map.addMarker(markerOptions).showInfoWindow();
                testpolyline.add(data_latlng.get(i));
            }

        }

        //맵에 폴리라인 표시
        Polyline polyline = map.addPolyline(testpolyline);



        //거리계산
//        double tempdistance = SphericalUtil.computeDistanceBetween(tempplace, tempplace2);
        double testdistance = 0;
        for (int i=0; i<data_latlng.size()-1; i++){
            testdistance += SphericalUtil.computeDistanceBetween(data_latlng.get(i), data_latlng.get(i+1));
        }



        //거리계산 후 이동거리 표시
        if ( (int) testdistance < 1000){
            distance.setText("총 이동거리 - " + (int) testdistance + "m");
        }else{
            int tempdistance = (int)testdistance;
            String str = String.format("%,d", tempdistance);
            Log.e("Distance Log : ", str);
            str =str.replace(",", ".");
            Log.e("Distance Log : ", str);
            distance.setText("총 이동거리 - " + str + "km");
        }

        dialog.dismiss();

    }


    public void getData(String url, String id, String time1, String time2){
        class GetDataJSON extends AsyncTask<String, Void, String>{

//            ProgressDialog dialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(DailyLocation.this, "","잠시만 기다려 주세요 ...", true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String id = params[1];
                String time1 = params[2];
                String time2 = params[3];





                BufferedReader bufferedReader = null;
                try{

                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("time1", "UTF-8") + "=" + URLEncoder.encode(time1, "UTF-8");
                    data += "&" + URLEncoder.encode("time2", "UTF-8") + "=" + URLEncoder.encode(time2, "UTF-8");

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
        g.execute(url, id, time1, time2);

    }

    private String findAddress(LatLng latlng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;

        String[] templatlng = latlng.toString().split("[(]");
        String[] templatlng2 = templatlng[1].split("[)]");
        String[] templatlng3 = templatlng2[0].split(",");
        String templat = templatlng3[0];
        String templng = templatlng3[1];

        Log.e("templat : ", templat);
        Log.e("templng : ", templng);

//        String[] templatlng = latlng.toString().split(",");



        double lat = Double.parseDouble(templat);
        double lng = Double.parseDouble(templng);
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAdminArea()
                            + ' ' + address.get(0).getLocality()
                            + ' ' + address.get(0).getThoroughfare()
                            + ' ' + address.get(0).getFeatureName();
                    bf.append(currentLocationAddress);

                }
            }

        } catch (IOException e) {
            Toast.makeText(DailyLocation.this, "주소변환실패", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return bf.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }


}
