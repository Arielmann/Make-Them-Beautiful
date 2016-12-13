package com.example.home.makethembeautiful.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.home.makethembeautiful.R;

import com.example.home.makethembeautiful.profile.onclickmethods.SettingsButton;


/*
* NOTE: This class is not active yet
* */
public class ProfileInfoScreen extends AppCompatActivity {
    private TextView name;
    private TextView company;
    private TextView location;
    private TextView website;
    private Button editNameCompany;
    private Button editLocWeb;
    private SettingsButton settings;
    private Button goToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        name = (TextView) findViewById(R.id.stylistName);
        company = (TextView) findViewById(R.id.stylistCompany);
        location = (TextView) findViewById(R.id.stylistLocation);
        //website = (TextView) findViewById(R.id.stylistWebsite);
        editNameCompany = (Button) findViewById(R.id.editNameCompany);
        editLocWeb = (Button) findViewById(R.id.editNameCompany);
        settings = new SettingsButton(this);
        goToSettings = (Button) findViewById(R.id.settings);
    //    goToSettings.setOnClickListener(settings);
    }
}

