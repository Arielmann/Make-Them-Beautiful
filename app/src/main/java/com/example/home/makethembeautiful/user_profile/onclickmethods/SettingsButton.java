package com.example.home.makethembeautiful.user_profile.onclickmethods;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.home.makethembeautiful.user_profile.user_info.SettingsMainScreen;

/**
 * Created by home on 4/13/2016.
 */

/*
* NOTE: This class is not active yet
* */
public class SettingsButton implements View.OnClickListener {
    Context context;

    public SettingsButton(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent goToAdvancedSettings = new Intent(context, SettingsMainScreen.class);
    }
}
