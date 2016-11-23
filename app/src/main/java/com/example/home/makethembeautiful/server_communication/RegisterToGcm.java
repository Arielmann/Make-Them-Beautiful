package com.example.home.makethembeautiful.server_communication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.home.makethembeautiful.user_profile.SharedPrefrences.SharedPrefManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by home on 5/25/2016.
 */
public class RegisterToGcm extends AsyncTask<Void, Void, String> {
    String REGISTER_GCM_TAG = "Register To Gcm: ";
    Context context;

    public RegisterToGcm(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        // id for this application in this device
        InstanceID instanceID = InstanceID.getInstance(context);
        String gcmToken = null;
        try {
            gcmToken = instanceID.getToken(Config.SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gcmToken;
    }

    @Override
    protected void onPostExecute(String gcmToken) {
        SharedPrefManager.getInstance(context).saveStringInfoToSharedPreferences(context, "gcmToken", gcmToken);
        Log.d(REGISTER_GCM_TAG, "Token from shared prefrences: " + SharedPrefManager.getInstance(context).getUserGcmToken());

    }
}


