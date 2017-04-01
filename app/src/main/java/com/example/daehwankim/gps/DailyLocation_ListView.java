package com.example.daehwankim.gps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daehwan.Kim on 2016-05-14.
 */
public class DailyLocation_ListView extends ActionBarActivity {


    String myjson;

    ProgressDialog dialog;

    private static final String TAG_RESULT = "result";
    private static final String TAG_time = "time";
    private static final String TAG_latitude = "latitude";
    private static final String TAG_longitude = "longitude";
    private static final String TAG_address = "address";

    JSONArray GET_info = null;

    ArrayList<HashMap<String, String>> GET_info_List;

    ArrayList<String> data_time = new ArrayList<String>();
    ArrayList<LatLng> data_latlng = new ArrayList<LatLng>();
    ArrayList<String> data_address = new ArrayList<String>();

    String time;
    String time1;
    String time2;


    ArrayList<DailyLocation_ListView_Data> datas = new ArrayList<DailyLocation_ListView_Data>();
    DailyLocation_ListView_BaseAdapter adapter;
    ListView listView;

    TextView start_time;
    TextView end_time;
    TextView start_day;
    TextView end_day;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailylocation_listview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(R.id.listView);
        start_time = (TextView) findViewById(R.id.textView15);
        end_time = (TextView) findViewById(R.id.textView16);
        start_day = (TextView) findViewById(R.id.textView13);
        end_day = (TextView) findViewById(R.id.textView14);
        Button check = (Button) findViewById(R.id.button8);

        String starttime;
        String endtime;



        Date date = new Date();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat min = new SimpleDateFormat("mm");
        SimpleDateFormat tempday = new SimpleDateFormat("yyyyMMdd");
        time = tempday.format(date);
        time1 = time + "0000";
        time2 = time + "5959";
        Log.e("DailyLocation Log(time)", time1);
        Log.e("DailyLocation Log(time)", time2);
        GET_info_List = new ArrayList<HashMap<String, String>>();

        start_day.setText(year.format(date) + "년 " + month.format(date) + "월" + day.format(date) +"일");
        start_time.setText(" 00시00분");
        end_day.setText(year.format(date) + "년 " + month.format(date) + "월" + day.format(date) +"일");
        end_time.setText(" " + hour.format(date) + "시" + min.format(date) + "분");


        start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

        end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(3);
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(4);
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 조회 시작일이 끝일보다 큰지 작은지 비교
                SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
                SharedPreferences.Editor ideditor = idpref.edit();
                if (    Long.valueOf(start_day.getText().toString().substring(0,4)
                        + start_day.getText().toString().substring(6,8)
                        + start_day.getText().toString().substring(9,11)
                        + start_time.getText().toString().substring(1,3)
                        + start_time.getText().toString().substring(4,6))
                                                <
                        Long.valueOf(end_day.getText().toString().substring(0,4)
                        + end_day.getText().toString().substring(6,8)
                        + end_day.getText().toString().substring(9,11)
                        + end_time.getText().toString().substring(1,3)
                        + end_time.getText().toString().substring(4,6)))
                {
                    getData("http://miraclehwan.vps.phps.kr/GPS/get_daily_location.php", idpref.getString("id", ""),
                            start_day.getText().toString().substring(0,4)
                                    + start_day.getText().toString().substring(6,8)
                                    + start_day.getText().toString().substring(9,11)
                                    + start_time.getText().toString().substring(1,3)
                                    + start_time.getText().toString().substring(4,6),
                            end_day.getText().toString().substring(0,4)
                                    + end_day.getText().toString().substring(6,8)
                                    + end_day.getText().toString().substring(9,11)
                                    + end_time.getText().toString().substring(1,3)
                                    + end_time.getText().toString().substring(4,6));
                    dialog = ProgressDialog.show(DailyLocation_ListView.this, "","잠시만 기다려 주세요 ...", true);
                }else{
                    Toast.makeText(DailyLocation_ListView.this, "조회기간이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected Dialog onCreateDialog(int id) {

        Date date = new Date();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        switch (id) {
            case 1:
                DatePickerDialog datePickerDialog = new DatePickerDialog(DailyLocation_ListView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String temp_month = String.valueOf(monthOfYear+1);
                        String temp_day = String.valueOf(dayOfMonth);
                        if (Integer.valueOf(monthOfYear+1) < 10){
                            temp_month = "0" + temp_month;
                            Log.e("time Log : ",year + "년 " + temp_month + "월" + temp_day + "일");
                        }
                        if (Integer.valueOf(dayOfMonth) < 10){
                            temp_day = "0" + temp_day;
                            Log.e("time Log : ",year + "년 " + temp_month + "월" + temp_day + "일");
                        }

                        start_day.setText(year + "년 " + temp_month + "월" + temp_day + "일");
                        Log.e("time error Log : ",year + "년 " + temp_month + "월" + temp_day + "일");
                    }
                }, Integer.parseInt(year.format(date)), (Integer.parseInt(month.format(date)) - 1), Integer.parseInt(day.format(date)));
                Log.e("YEAR log : ", year.format(date));
                Log.e("YEAR log : ", month.format(date));
                Log.e("YEAR log : ", day.format(date));
                return datePickerDialog;

            case 2:
                TimePickerDialog timePickerDialog = new TimePickerDialog(DailyLocation_ListView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String temp_hour = String.valueOf(hourOfDay);
                        String temp_minute = String.valueOf(minute);
                        if (Integer.valueOf(hourOfDay) < 10){
                            temp_hour = "0" + temp_hour;
                        }
                        if (Integer.valueOf(minute) < 10){
                            temp_minute = "0" + temp_minute;
                        }
                        start_time.setText(" " + temp_hour + "시" + temp_minute + "분");
                    }
                }, 0, 00 ,false);
                return timePickerDialog;
            case 3:
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(DailyLocation_ListView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String temp_month = String.valueOf(monthOfYear+1);
                        String temp_day = String.valueOf(dayOfMonth);
                        if (monthOfYear+1 < 10){
                            temp_month = "0" + temp_month;
                        }
                        if (dayOfMonth < 10){
                            temp_day = "0" + temp_day;
                        }

                        end_day.setText(year + "년 " + temp_month + "월" + temp_day + "일");
                    }
                }, Integer.parseInt(year.format(date)), (Integer.parseInt(month.format(date)) - 1), Integer.parseInt(day.format(date)));
                Log.e("YEAR log : ", year.format(date));
                Log.e("YEAR log : ", month.format(date));
                Log.e("YEAR log : ", day.format(date));
                return datePickerDialog2;

            case 4:
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(DailyLocation_ListView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String temp_hour = String.valueOf(hourOfDay);
                        String temp_minute = String.valueOf(minute);
                        if (Integer.valueOf(hourOfDay) < 10){
                            temp_hour = "0" + temp_hour;
                        }
                        if (Integer.valueOf(minute) < 10){
                            temp_minute = "0" + temp_minute;
                        }
                        end_time.setText(" " + temp_hour + "시" + temp_minute + "분");
                    }
                }, 0, 00 ,false);

                return timePickerDialog2;



        }
        return super.onCreateDialog(id);
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
                data_address.add(i, findAddress(new LatLng(Double.parseDouble(json_latitude), Double.parseDouble(json_longitude))));


                HashMap<String,String> json_data = new HashMap<String,String>();

                json_data.put(TAG_time, json_time);
                json_data.put(TAG_latitude, json_latitude);
                json_data.put(TAG_longitude, json_longitude);
                json_data.put(TAG_address, findAddress(new LatLng(Double.parseDouble(json_latitude), Double.parseDouble(json_longitude))));

                GET_info_List.add(json_data);
            }


            Log.e("JSON time Log : ", data_time.toString());
            Log.e("JSON latlng Log : ", data_latlng.toString());


            for (int i=0; i<data_time.size(); i++){
                Log.e("time Log : ", data_time.get(i) );
                datas.add(new DailyLocation_ListView_Data(        	data_time.get(i).substring(0,4) + "년 "
                                                                    + data_time.get(i).substring(4,6) + "월"
                                                                    + data_time.get(i).substring(6,8) + "일 "
                                                                    + data_time.get(i).substring(8,10) + "시"
                                                                    + data_time.get(i).substring(10,12) + "분", data_address.get(i)));
            }


            adapter = new DailyLocation_ListView_BaseAdapter(getLayoutInflater(), datas);

            listView.setAdapter(adapter);


            dialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData(String url, String id, String time1, String time2){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

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


        double lat = Double.parseDouble(templat);
        double lng = Double.parseDouble(templng);
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
            Toast.makeText(DailyLocation_ListView.this, "주소실패", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return bf.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Long start = Long.valueOf(start_day.getText().toString().substring(0,4)
//                + start_day.getText().toString().substring(6,8)
//                + start_day.getText().toString().substring(9,11)
//                + start_time.getText().toString().substring(1,3)
//                + start_time.getText().toString().substring(4,6));
//        Long end = Long.valueOf(end_day.getText().toString().substring(0,4)
//                + end_day.getText().toString().substring(6,8)
//                + end_day.getText().toString().substring(9,11)
//                + end_time.getText().toString().substring(1,3)
//                + end_time.getText().toString().substring(4,6));
//
//        Log.e("Start Log : ", start.toString());
//        Log.e("End Log : ", end.toString());
//
//        if(start > end){
//            Toast.makeText(DailyLocation_ListView.this, "조회기간이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
//        }else if((end-start > 2400)){
//            Toast.makeText(DailyLocation_ListView.this, "최대 24시간까지 조회가 가능합니다.", Toast.LENGTH_SHORT).show();
//        }


    }
}