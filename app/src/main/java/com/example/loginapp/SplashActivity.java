package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.loginapp.utils.AppPreferences;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isLoggedIn= AppPreferences.getInstance(this).getBoolean(AppPreferences.Keys.IS_LOGGED_IN);

        Intent intent;

        if(!isLoggedIn){
            intent= new Intent(this,LoginActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
