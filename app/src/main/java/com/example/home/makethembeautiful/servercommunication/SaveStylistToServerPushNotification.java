package com.example.home.makethembeautiful.servercommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.home.makethembeautiful.profile.sharedprefrences.SharedPrefManager;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by home on 5/8/2016.
 */
public class SaveStylistToServerPushNotification extends AsyncTask<String, Void, Boolean> {

    private static final String SAVE_USER_TAG = "Save stylist to server";
    Context context;
    OnPushNotificationSent onPushSent;

    public SaveStylistToServerPushNotification(Context context, OnPushNotificationSent interfaceHolder) {
        this.context = context;
        this.onPushSent = interfaceHolder;
    }

    @Override
    protected Boolean doInBackground(final String... strings) {
        HashMap<String, String> userData = SharedPrefManager.getInstance(context).initSharedPrefData();
        OkHttpClient client = new OkHttpClient();
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //TODO: find out the meaning of the extra string when creating a RequestBody.
                .addFormDataPart("FindMeaning", "FindMeaningValue",
                        RequestBody.create(MediaType.parse("text/csv"), "FindMeaningValue"))
                .addFormDataPart("name", userData.get("name"))
                .addFormDataPart("password", userData.get("password"))
                .addFormDataPart("email", userData.get("email"))
                .addFormDataPart("company", userData.get("company"))
                .addFormDataPart("location", userData.get("location"))
                .addFormDataPart("website", userData.get("website"))
                .addFormDataPart("description", userData.get("description"))
                .addFormDataPart("profileImageUrl", userData.get("profileImageUrl"))
                .addFormDataPart("gcmToken", userData.get("gcmToken"))
                .build();

        Request request = new Request.Builder()
                .url(Config.SAVE_STYLIST)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                onPushSent.onPushFailure();
                Log.d(SAVE_USER_TAG, "UPLOAD FAILED. Details: " + e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onPushSent.onPushFailure();
                    Log.d(SAVE_USER_TAG, "UPLOAD FAILED. Details: " + response.body().string());
                }
                onPushSent.onPushSuccess();
                Log.d(SAVE_USER_TAG, "UPLOAD SUCCESSFUL! Details: " + response.body().string());
            }
        });
        return true;
    }
}
