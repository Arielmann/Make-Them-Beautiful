package com.example.home.makethembeautiful.profile.profilemodels;

import android.graphics.Bitmap;

import com.example.home.makethembeautiful.imageutils.ImageUtils;

import java.io.Serializable;

/**
 * Created by home on 4/14/2016.
 */
public class User implements Serializable {

    private int id;
    private String name;
    private String location;
    private String profileImageFilePath;
    private String gcmToken;
    private String description;
    private String profileImageUrl;
    private Bitmap userImageBitmap;

    public User(int id, String name, String location, String profileImageUrl, String description, String token) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
        this.profileImageFilePath = "none (Defined in User class)";
        this.description = description;
        this.gcmToken = token;
        this.userImageBitmap = ImageUtils.defaultProfileImage;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public String getProfileImagePath() {
        return profileImageFilePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfileImageFilePath(String profileImageFilePath) {
        this.profileImageFilePath = profileImageFilePath;
    }

    public void setUserImageBitmap(Bitmap userImageBitmap) {
        this.userImageBitmap = userImageBitmap;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    @Override
    public String toString() {
        return name + " from " + location + " Profile image file:" + profileImageFilePath
                + " Token " + gcmToken + " Description: " + description;
    }

    public Bitmap getUserImageBitmap() {
        return userImageBitmap;
    }
}
