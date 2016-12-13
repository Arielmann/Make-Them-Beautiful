package com.example.home.makethembeautiful.profile.profilemodels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by home on 4/3/2016.
 */
public class Stylist extends User {
    private String company;
    private String website;

    public Stylist(int id, String name, String company, String location, String profileImageUrl, String description, String website, String token) {
        super(id, name, location, profileImageUrl, description, token);
        this.company = company;
        this.website = website;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    protected JSONObject toJson() {
     JSONObject stylistObject = new JSONObject();
        try {
            stylistObject.put("name", getName());
            stylistObject.put("company", company);
            stylistObject.put("location", getLocation());
            stylistObject.put("website", website);
            return stylistObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

