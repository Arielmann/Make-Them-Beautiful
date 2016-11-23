package com.example.home.makethembeautiful.user_profile.SharedPrefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.HashMap;

/**
 * Created by home on 5/21/2016.
 */
public class SharedPrefManager {
    /*
    * Shared preferences of this app saves profile info and has
    * id
    * name
    * password
    * email
    * company name
    * location
    * website
    * description
    * profile image file path
    * profile image url
    * gcm token
    * device screen's height
    * device screen's width
    *
    * these properties will be gettable throughout this class
    * */

    // private static ImageArray sharedPrefs = new ImageArray();
    private static SharedPrefManager sharedPrefs;
    private SharedPreferences insideSharedPref;
    private int notificationsCounter;
    private Bitmap userImageBitmap;
    private String ACCOUNT_INFO = "Account info";

    private SharedPrefManager(Context context) {
        insideSharedPref = context.getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
    } //creates the only instance

    // prevents creating of instances
    public static SharedPrefManager getInstance(Context context) { // create a static common database
        if (sharedPrefs == null) {
            sharedPrefs = new SharedPrefManager(context);
        }
        return sharedPrefs;
    }

    public SharedPreferences getSharedPrefs() {
        return insideSharedPref;
    }

    public int getUserId() {
        return insideSharedPref.getInt("id", -1);
    }

    public String getUserName() {
        return insideSharedPref.getString("name", "user name error");
    }

    public String getUserPassword() {
        return insideSharedPref.getString("password", "password error");
    }

    public String getUserEmail() {
        return insideSharedPref.getString("email", "email error");
    }

    public String getCompanyName() {
        return insideSharedPref.getString("company", "company error");
    }

    public String getCompanyLocation() {
        return insideSharedPref.getString("location", "location error");
    }

    public String getCompanyWebsite() {
        return insideSharedPref.getString("website", "website error");
    }

    public String getProfileImagePath() {
        return insideSharedPref.getString("profileImageFilePath", "profile Image File Path error");
    }

    public String getProfileImageUrl() {
        return insideSharedPref.getString("profileImageUrl", "profile Image Url error");
    }

    public String getCompanyDescription() {
        return insideSharedPref.getString("description", "description error");
    }

    public String getUserGcmToken() { //DELETE METHOD - TESTS ONLY!!
        return insideSharedPref.getString("gcmToken", "gcm token error");
    }

    public int getUserDeviceScreenHeight() {
        return insideSharedPref.getInt("deviceScreenHeight", -1);
    }
    public int getUserDeviceScreenWidth() {
        return insideSharedPref.getInt("deviceScreenWidth", -1);
    }

    public boolean isContactedUsersScreenActive() {
        return insideSharedPref.getBoolean("isContactedUsersScreenActive", false);
    }

    public Bitmap getUserImageBitmap(){
        return userImageBitmap;
    }

    public void setUserImageBitmap(Bitmap userImageBitmap) {
        this.userImageBitmap = userImageBitmap;
    }
    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = insideSharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveStringInfoToSharedPreferences(final Context context, final String key, final String value) {
        AsyncTask saveToSharedPref = new AsyncTask() {

            @Override
            protected Object doInBackground(Object... params) {
                SharedPreferences.Editor editor = SharedPrefManager.getInstance(context).getSharedPrefs().edit()    //SharedPrefManager is a LOCAL class
                        .putString(key, value);
                editor.commit();
                return null;
            }
        };
        saveToSharedPref.execute();
    }

    public void saveIntInfoToSharedPreferences(final Context context, final String key, final int value) {
        AsyncTask saveToSharedPref = new AsyncTask() {

            @Override
            protected Object doInBackground(Object... params) {
                SharedPreferences.Editor editor = SharedPrefManager.getInstance(context).getSharedPrefs().edit()    //SharedPrefManager is a LOCAL class
                        .putInt(key, value);
                editor.commit();
                return null;
            }
        };
        saveToSharedPref.execute();
    }

    public void saveStringInfoToSharedPreferences(final Context context, final String key, final boolean value) {
        AsyncTask saveToSharedPref = new AsyncTask() {

            @Override
            protected Object doInBackground(Object... params) {
                SharedPreferences.Editor editor = SharedPrefManager.getInstance(context).getSharedPrefs().edit()    //SharedPrefManager is a LOCAL class
                        .putBoolean(key, value);
                editor.commit();
                return null;
            }
        };
        saveToSharedPref.execute();
    }

    public HashMap<String, String> initSharedPrefData() {
        HashMap<String, String> sharedPrefData = new HashMap<>();
        sharedPrefData.put("name", insideSharedPref.getString("name", "error"));
        sharedPrefData.put("password", insideSharedPref.getString("password", "error"));
        sharedPrefData.put("email",  insideSharedPref.getString("email", "error"));
        sharedPrefData.put("company",  insideSharedPref.getString("company", "error"));
        sharedPrefData.put("location", insideSharedPref.getString("location", "error"));
        sharedPrefData.put("website", insideSharedPref.getString("website", "error"));
        sharedPrefData.put("profileImageFilePath", insideSharedPref.getString("profileImageFilePath", "error"));
        sharedPrefData.put("profileImageUrl", insideSharedPref.getString("profileImageUrl", "error"));
        sharedPrefData.put("description", insideSharedPref.getString("description", "error"));
        sharedPrefData.put("gcmToken", insideSharedPref.getString("gcmToken", "error"));
        return sharedPrefData;
    }


    public int getNotificationsCounter() {
        return notificationsCounter++;
    }

    public String getProfileImagesDir() {
        return "/Make_Them_Beautiful/Contacts";
    }

    @Override
    public String toString() {
        return "Stylist details: " +
                " Name: " + getUserName() +
                " Company: " + getCompanyName() +
                " Location: " + getUserName() +
                " Description: " + getCompanyDescription() +
                " Website: " + getCompanyWebsite() +
                " Profile image file: " + getProfileImagePath() +
                " Profile image url: " + getProfileImageUrl() +
                " Gcm Token: " + getUserGcmToken() +

                " Device details: " +
                " Screen Height: " + getUserDeviceScreenHeight() +
                " Screen Width: " + getUserDeviceScreenWidth();
    }
}

