package com.example.home.makethembeautiful.profile.registration.basic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.home.makethembeautiful.appinit.AppDataInit;


public class RegisterBasicProfileScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDataInit.initCrashesMonitor(this);
        AppDataInit.createDirectories();
        setContentView(com.example.home.makethembeautiful.R.layout.activity_register_basic_profile);
    }
}



