package com.example.daehwankim.gps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.AsyncTaskCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Daehwan.Kim on 2016-04-24.
 */
public class Join extends FragmentActivity {


    private EditText id;
    private EditText password;
    private EditText verifypassword;
    private EditText nickname;
    private boolean verifyID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        id = (EditText) findViewById(R.id.editText7);
        password = (EditText) findViewById(R.id.editText8);
        verifypassword = (EditText) findViewById(R.id.editText9);
        nickname = (EditText) findViewById(R.id.editText10);
        Button join_button = (Button) findViewById(R.id.button7);
        Button verifyID_button = (Button) findViewById(R.id.button6);

        // ID 입력에 변화가 있으면 false로 변경
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                verifyID = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verifyID_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().length() > 0){
                    check(v);
                }else{
                    Toast.makeText(Join.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyID==true){
                    if (password.getText().toString().equals(verifypassword.getText().toString()) && password.getText().length() > 0){
                        if (nickname.getText().length() > 0){
                            insert(v);
                        }else{
                            Toast.makeText(Join.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Join.this, "패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Join.this, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void insert(View view){
        String get_id = id.getText().toString();
        String get_password = password.getText().toString();
        String get_nickname = nickname.getText().toString();

        insertToDatabase(get_id, get_password, get_nickname);

    }

    private void insertToDatabase(String id, String password, String nickname){

        class InsertData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override // 결과출력
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(),"가입완료",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String password = (String)params[1];
                    String nickname = (String)params[2];

                    String link="http://115.71.238.147/GPS/join_query.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8");

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

        InsertData task = new InsertData();
        task.execute(id, password, nickname);
    }




    public void check(View view){
        String get_id = id.getText().toString();

        CheckToDatabase(get_id);

    }

    private void CheckToDatabase(final String id){

        class checkData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override // 결과출력
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals(id+"ok")) {
                    Toast.makeText(getApplicationContext(), "중복된 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    verifyID = true;
                }
            }

            @Override
            protected String doInBackground(String... params) {



                try{
                    String id = (String)params[0];

                    String link="http://115.71.238.147/GPS/joinpage_verifyID_check.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
                    Log.e("sbString Log", sb.toString());;
                    Log.e("sbString Log 아랫줄", "-----------------");
                    return sb.toString();


                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        checkData task = new checkData();
        task.execute(id);
    }



}
