package com.example.daehwankim.gps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daehwan.Kim on 2016-05-22.
 */
public class Temp_Location_Capture extends FragmentActivity {

    GoogleMap map;
    GpsInfo gps;
    double latitude;
    double longitude;
    String address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_location_capture);

        Intent get_intent = getIntent();


        gps = new GpsInfo(Temp_Location_Capture.this);
        latitude = get_intent.getDoubleExtra("lat", 0);
        longitude = get_intent.getDoubleExtra("long", 0);



        final LatLng userlocation = new LatLng(latitude, longitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();





        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 16));

        Marker me = map.addMarker(new MarkerOptions().position(userlocation).title(findAddress(latitude, longitude)));
        me.showInfoWindow();


        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/GPS";
        FrameLayout linearLayout = (FrameLayout) findViewById(R.id.temp_capture);

        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }

        SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        linearLayout.buildDrawingCache();
        Bitmap capture_view = linearLayout.getDrawingCache();

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(path+"/Capture"+day.format(date)+".jpeg");
            capture_view.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            linearLayout.destroyDrawingCache();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

            e.printStackTrace();
        }
        return bf.toString();
    }

    private void capture(){

    }



}
