package com.example.daehwankim.gps;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login extends FragmentActivity {

    EditText id;
    EditText password;
    boolean logincheck = false;
    CheckBox save_checkbox;
    public static String user_id;
    String myjson2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id =  (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        save_checkbox = (CheckBox) findViewById(R.id.checkBox);
        Button loginbutton = (Button) findViewById(R.id.button);
        Button joinbutton = (Button) findViewById(R.id.button2);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logincheck=false;
                if (id.getText().length() > 0){
                    if(password.getText().length() > 0){

                        check(v);

                    }else{
                        Toast.makeText(Login.this, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Login.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_join_page = new Intent(Login.this, Join.class);
                startActivity(move_join_page);
            }
        });


    }


    public void check(View view){
        String get_id = id.getText().toString();
        String get_password = password.getText().toString();
        CheckToDatabase(get_id, get_password);

    }

    private void CheckToDatabase(final String id, final String password){

        class checkData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override // 결과출력
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.toString().equals("success")){
                    logincheck=true;
                }else{
                    logincheck=false;
                }

                if(logincheck){
                    if(save_checkbox.isChecked() == true){
                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("id", id.toString());
                        editor.putString("password", password.toString());
                        editor.commit();
                    }


                    //아이디저장
                    SharedPreferences idpref = getSharedPreferences("idpref", MODE_PRIVATE);
                    SharedPreferences.Editor ideditor = idpref.edit();
                    ideditor.putString("id", id.toString());
                    ideditor.commit();

                    password_check("http://miraclehwan.vps.phps.kr/GPS/second_password_check.php",idpref.getString("id", ""), "0");


                }else{
                    Toast.makeText(Login.this, "아이디, 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {



                try{
                    String id = (String)params[0];
                    String password = (String)params[1];
                    String link="http://115.71.238.147/GPS/login_check.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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


                    Log.e("sbString Log 윗줄", "-----------------");
                    Log.e("sbString Log", sb.toString());
                    Log.e("sbString Log 아랫줄", "-----------------");
                    return sb.toString();


                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        checkData task = new checkData();
        task.execute(id, password);
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        id.setText(pref.getString("id", ""));
        password.setText(pref.getString("password",""));
        Log.e("Login Log : ", "id = " + pref.getString("id","") + " password = " + pref.getString("password",""));
        editor.clear();
        editor.commit();
        if(id.getText().length() >= 1){
            save_checkbox.setChecked(true);
        }

        if(id.getText().length() > 0 && password.getText().length() >0 && save_checkbox.isChecked() ==true){
            String get_id = id.getText().toString();
            String get_password = password.getText().toString();
            CheckToDatabase(get_id, get_password);
        }

    }

    protected void onStop() {
        super.onStop();

        if(save_checkbox.isChecked() == true){
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("id", id.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.commit();
        }

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
                    Intent move_second_password_page = new Intent(Login.this, Second_password.class);
                    startActivity(move_second_password_page);
                    finish();

                }else{
                    Intent move_menu_page = new Intent(Login.this, Nevigation_Bar_Menu.class);
                    startActivity(move_menu_page);
                    finish();
                }

            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, id, password);

    }

}
