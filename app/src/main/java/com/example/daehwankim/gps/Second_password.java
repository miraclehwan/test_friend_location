package com.example.daehwankim.gps;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Daehwan.Kim on 2016-04-10.
 */
public class Second_password extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_password);

        final EditText one = (EditText) findViewById(R.id.editText3);
        final EditText two = (EditText) findViewById(R.id.editText4);
        final EditText three = (EditText) findViewById(R.id.editText5);
        final EditText four = (EditText) findViewById(R.id.editText6);

        final ImageView num1 = (ImageView) findViewById(R.id.imageView2);
        final ImageView num2 = (ImageView) findViewById(R.id.imageView3);
        final ImageView num3 = (ImageView) findViewById(R.id.imageView4);
        final ImageView num4 = (ImageView) findViewById(R.id.imageView5);
        final ImageView num5 = (ImageView) findViewById(R.id.imageView6);
        final ImageView num6 = (ImageView) findViewById(R.id.imageView7);
        final ImageView num7 = (ImageView) findViewById(R.id.imageView8);
        final ImageView num8 = (ImageView) findViewById(R.id.imageView9);
        final ImageView num9 = (ImageView) findViewById(R.id.imageView10);
        final ImageView num0 = (ImageView) findViewById(R.id.imageView12);

        num1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num1.setImageResource(R.drawable.aa);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num1.setImageResource(R.drawable.a);
                }
                return false;
            }
        });

        num2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num2.setImageResource(R.drawable.bb);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num2.setImageResource(R.drawable.b);
                }
                return false;
            }
        });

        num3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num3.setImageResource(R.drawable.cc);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num3.setImageResource(R.drawable.c);
                }
                return false;
            }
        });

        num4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num4.setImageResource(R.drawable.dd);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num4.setImageResource(R.drawable.d);
                }
                return false;
            }
        });

        num5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num5.setImageResource(R.drawable.ee);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num5.setImageResource(R.drawable.e);
                }
                return false;
            }
        });

        num6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num6.setImageResource(R.drawable.ff);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num6.setImageResource(R.drawable.f);
                }
                return false;
            }
        });

        num7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num7.setImageResource(R.drawable.gg);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num7.setImageResource(R.drawable.g);
                }
                return false;
            }
        });

        num8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num8.setImageResource(R.drawable.hh);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num8.setImageResource(R.drawable.h);
                }
                return false;
            }
        });

        num9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num9.setImageResource(R.drawable.ii);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num9.setImageResource(R.drawable.i);
                }
                return false;
            }
        });

        num0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN ){
                    num0.setImageResource(R.drawable.jj);
                }

                if ( event.getAction() == MotionEvent.ACTION_UP ){
                    num0.setImageResource(R.drawable.j);
                }
                return false;
            }
        });

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("1");
                }else if ( two.length() == 0 ){
                    two.setText("1");
                }else if ( three.length() == 0){
                    three.setText("1");
                }else{
                    four.setText("1");
                }
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("2");
                }else if ( two.length() == 0 ){
                    two.setText("2");
                }else if ( three.length() == 0){
                    three.setText("2");
                }else{
                    four.setText("2");
                }
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("3");
                }else if ( two.length() == 0 ){
                    two.setText("3");
                }else if ( three.length() == 0){
                    three.setText("3");
                }else{
                    four.setText("3");
                }
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("4");
                }else if ( two.length() == 0 ){
                    two.setText("4");
                }else if ( three.length() == 0){
                    three.setText("4");
                }else{
                    four.setText("4");
                }
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("5");
                }else if ( two.length() == 0 ){
                    two.setText("5");
                }else if ( three.length() == 0){
                    three.setText("5");
                }else{
                    four.setText("5");
                }
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("6");
                }else if ( two.length() == 0 ){
                    two.setText("6");
                }else if ( three.length() == 0){
                    three.setText("6");
                }else{
                    four.setText("6");
                }
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("7");
                }else if ( two.length() == 0 ){
                    two.setText("7");
                }else if ( three.length() == 0){
                    three.setText("7");
                }else{
                    four.setText("7");
                }
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("8");
                }else if ( two.length() == 0 ){
                    two.setText("8");
                }else if ( three.length() == 0){
                    three.setText("8");
                }else{
                    four.setText("8");
                }
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("9");
                }else if ( two.length() == 0 ){
                    two.setText("9");
                }else if ( three.length() == 0){
                    three.setText("9");
                }else{
                    four.setText("9");
                }
            }
        });

        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( one.length() == 0 ){
                    one.setText("0");
                }else if ( two.length() == 0 ){
                    two.setText("0");
                }else if ( three.length() == 0){
                    three.setText("0");
                }else{
                    four.setText("0");
                }
            }
        });


        one.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        two.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        three.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        four.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( one.length() == 1 ){
                    two.requestFocus();
                } else {
                    one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( two.length() == 1 ){
                    three.requestFocus();
                } else {
                    two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( three.length() == 1 ){
                    four.requestFocus();
                } else {
                    three.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (four.length() == 0) {

                } else {
                    if (one.getText().toString().equals("1") && two.getText().toString().equals("2") && three.getText().toString().equals("3") && four.getText().toString().equals("4")) {
                        Intent movemenu = new Intent(Second_password.this, Nevigation_Bar_Menu.class);
                        startActivity(movemenu);
                        finish();
                    } else {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(300);
                        Toast.makeText(Second_password.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        one.setText("");
                        two.setText("");
                        three.setText("");
                        four.setText("");
                        one.requestFocus();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
