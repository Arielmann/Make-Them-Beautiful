package com.example.home.makethembeautiful.profile.registration.basic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.view.Display;

import com.example.home.makethembeautiful.contactedusers.ContactedUsersActivity;
import com.example.home.makethembeautiful.servercommunication.RegisterToGcm;
import com.example.home.makethembeautiful.utils.handlers.GoToScreen;
import com.example.home.makethembeautiful.profile.sharedprefrences.SharedPrefManager;
import com.example.home.makethembeautiful.profile.registration.GenericSettingsModel;
import com.example.home.makethembeautiful.profile.registration.company.SetCompanyActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by home on 6/6/2016.
 */
public class RegisterBasicProfileModel extends GenericSettingsModel {
    private String name;
    private String password;
    private String email;

    public RegisterBasicProfileModel(Fragment signUpView) {
        super(signUpView);
        checkIfAlreadySignedUp(); //check if the Stylist is already signed up
    }

    @Override
    public void validate(HashMap<String, String> userInputs) {
        //TODO: check if name OR email already exists in the server's DB
        setUserInputs(userInputs);
        name = userInputs.get("name");
        password = userInputs.get("password");
        email = userInputs.get("email");


        if (name.length() < 3) {
            getErrors().put("nameError", "At least 3 characters");
            onDataChangeFailed();
            return;
        }

        if (name.equals("user name error")) {
            getErrors().put("nameError", "Invalid name");
            onDataChangeFailed();
            return;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            getErrors().put("passwordError", "Must be 4-10 alphanumeric characters");
            onDataChangeFailed();
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getErrors().put("emailError", "Enter a valid email address");
            onDataChangeFailed();
            return;
        }

        //SIGN UP SUCCEEDED!
        onDataChangeSucceeded();
    }

    @Override
    protected void onDataChangeSucceeded() {
        //*************Register info to databases using Asynctasks*****************
        RegisterToGcm registerToGcm = new RegisterToGcm(getContext());
        registerToGcm.execute(); //get a GCM token
        saveInsertedDataToSharedPref();
        saveScreenSizesToSharedPref((Activity) getContext());
        GoToScreen goToSetCompanyScreen = new GoToScreen((Activity) getContext(), SetCompanyActivity.class, "Creating Account...");
        goToSetCompanyScreen.onClick(null);
        getErrors().clear();
    }

    protected void onDataChangeFailed() {
        getViewsSetter().setViewUponFailedDataSetting(getErrors());
        getErrors().clear();
    }


    public void goToNextScreen() {
        Intent goToSetCompanyScreen = new Intent(getContext(), SetCompanyActivity.class);
        getContext().startActivity(goToSetCompanyScreen);
    }

    @Override
    public void saveInsertedDataToSharedPref() {
        for (Map.Entry<String, String> userInputsEntry : getUserInputs().entrySet()) {
            SharedPrefManager.getInstance(getContext()).saveStringInfoToSharedPreferences(getContext(), userInputsEntry.getKey(), userInputsEntry.getValue());
        }
    }

    private void checkIfAlreadySignedUp() {
        String userName = SharedPrefManager.getInstance(getContext()).getUserName();
        if (!userName.equals("user name error")) {
            Intent goToSetContactedUsersScreen = new Intent(getContext(), ContactedUsersActivity.class);
            getContext().startActivity(goToSetContactedUsersScreen);
        }
    }

    private void saveScreenSizesToSharedPref(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        SharedPrefManager.getInstance(activity).saveIntInfoToSharedPreferences(activity, "deviceScreenHeight", height);
        SharedPrefManager.getInstance(activity).saveIntInfoToSharedPreferences(activity, "deviceScreenWidth", width);
    }
}
