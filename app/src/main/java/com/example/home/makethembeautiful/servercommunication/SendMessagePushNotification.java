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

public class SendMessagePushNotification extends AsyncTask<Void, Void, Void> {

   /*
    * This class sends push notifications.
    * it sends data and provoke a server-side script which
    * notifies the gcm that the user has sent a message
    * */

    private String TAG = "pushNotification";
    private Context context;
    private String addressedRegId;
    private String messageText;
    private String messageImageUrl;

    public SendMessagePushNotification(Context context, String senderRegId, String imageUrl, String msg) {
        this.context = context;
        this.addressedRegId = senderRegId;
        this.messageImageUrl = imageUrl;
        this.messageText = msg;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HashMap<String, String> stringStringHashMap = SharedPrefManager.getInstance(context).initSharedPrefData();
        HashMap<String, String> userData = stringStringHashMap;

        String[] findMeaning = {"Idle"};

        OkHttpClient client = new OkHttpClient();
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //TODO: find out the meaning of the extra string when creating a RequestBody.
                .addFormDataPart("findMeaning", findMeaning[0],
                        RequestBody.create(MediaType.parse("text/csv"), findMeaning[0]))
                .addFormDataPart("sender_name", userData.get("name"))
                .addFormDataPart("company", userData.get("company"))
                .addFormDataPart("location", userData.get("location"))
                .addFormDataPart("profile_image_url", userData.get("profileImageUrl"))
                .addFormDataPart("description", userData.get("description"))
                .addFormDataPart("website", userData.get("website"))
                .addFormDataPart("sender_reg_id", userData.get("gcmToken"))
                .addFormDataPart("addressed_reg_id", addressedRegId)
                .addFormDataPart("msg_image_url", messageImageUrl)
                .addFormDataPart("msg_text", messageText)
                .build();

        final Request request = new Request.Builder()
                .url(Config.SEND_PUSH_NOTIFICATION)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "Failed to send message. error: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Failed to send message. response: " + response);
                }
                Log.d(response.body().string(), "Message was successfully sent");
            }
        });
        return null;
    }
}