package com.example.beguest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences onBoarding = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
        boolean isFirstTime = onBoarding.getBoolean("firstTime", true);

        //tirar
//        SharedPreferences.Editor editor = onBoarding.edit();
//        editor.putBoolean("firstTime", true);
//        editor.apply();

        if(isFirstTime){
            SharedPreferences.Editor editor = onBoarding.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, SignUp.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }
}