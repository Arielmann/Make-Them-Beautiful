package com.example.home.makethembeautiful.user_profile.user_info.company_info;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.home.makethembeautiful.user_profile.SharedPrefrences.SharedPrefManager;
import com.example.home.makethembeautiful.user_profile.user_info.GenericSettingsModel;
import com.example.home.makethembeautiful.user_profile.user_info.company_description.SetDescriptionScreen;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by home on 8/7/2016.
 */
public class SetCompanyModel extends GenericSettingsModel {

    private String companyName;
    private String location;
    private String website;

    public SetCompanyModel(Fragment setComapnyDataFrag) {
        super(setComapnyDataFrag);
    }

    @Override
    public void validate(HashMap<String, String> userInputs) {
        setUserInputs(userInputs);
        companyName = userInputs.get("company");
        location = userInputs.get("location");
        website = userInputs.get("website");

        if (companyName.isEmpty()) {
            getErrors().put("companyError", "Please enter your company's name");
            onDataChangeFailed();
            return;
        }

        if (location.isEmpty() || location.length() <= 1) {
            getErrors().put("locationError", "Please enter your company's location");
            onDataChangeFailed();
            return;
        }

        if (website.isEmpty()) { //TODO: add website validator
            getErrors().put("websiteError", "Please enter a valid url OR leave empty");
            onDataChangeFailed();
            return;
        }



        onDataChangeSucceeded();
    }


    @Override
    protected void onDataChangeSucceeded() {
        saveInsertedDataToSharedPref();
        goToNextScreen();
        getErrors().clear();
    }

    @Override
    protected void onDataChangeFailed() {
        getViewsSetter().setViewUponFailedDataSetting(getErrors());
        getErrors().clear();
    }

    @Override
    public void goToNextScreen() {
        Intent goToContactedUsersScreen = new Intent(getContext(), SetDescriptionScreen.class);
        getContext().startActivity(goToContactedUsersScreen);
    }

    @Override
    public void saveInsertedDataToSharedPref() {
        for(Map.Entry<String, String> stringEntry : getUserInputs().entrySet()) {
            SharedPrefManager.getInstance(getContext()).saveStringInfoToSharedPreferences(getContext(), stringEntry.getKey(), stringEntry.getValue());
        }
    }
}
