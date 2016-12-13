package com.example.home.makethembeautiful.profile.registration.company;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.profile.registration.GenericSettingsFrag;
import com.example.home.makethembeautiful.profile.registration.SetViewsAfterSignUpClicked;

import java.util.HashMap;

/**
 * Created by home on 8/7/2016.
 */
public class SetCompanyViewFrag extends GenericSettingsFrag implements View.OnClickListener, SetViewsAfterSignUpClicked {
    private EditText companyNameEditText;
    private EditText locationEditText;
    private EditText websiteEditText;
    private Button saveDetailsButton;

    @Override
    public void tryToUpdateData() {

        HashMap<String, String> userInputs = new HashMap<>();
        userInputs.put("company", companyNameEditText.getText().toString());
        userInputs.put("location", locationEditText.getText().toString());
        userInputs.put("website", websiteEditText.getText().toString());
        getModel().validate(userInputs);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View setCompanyLayout = inflater.inflate(R.layout.frag_register_company, null);
        companyNameEditText = (EditText) setCompanyLayout.findViewById(R.id.input_companyName);
        locationEditText = (EditText) setCompanyLayout.findViewById(R.id.input_companyLocation);
        websiteEditText = (EditText) setCompanyLayout.findViewById(R.id.input_companyWebsite);
        saveDetailsButton = (Button) setCompanyLayout.findViewById(R.id.btn_saveCompanyDetails);
        setModel(new SetCompanyModel(this));
        saveDetailsButton.setOnClickListener(this);
        return setCompanyLayout;
    }

    @Override
    public void onClick(View v) {
        saveDetailsButton.setEnabled(false);
        tryToUpdateData();
        saveDetailsButton.setEnabled(true);
    }

    //*************Interface's method is being called from model********//
    @Override
    public void setViewUponFailedDataSetting(HashMap<String, String> errors) {
        //set errors only if needed
        companyNameEditText.setError(errors.get("companyError"));
        locationEditText.setError(errors.get("locationError"));
        websiteEditText.setError(errors.get("websiteError"));
        saveDetailsButton.setEnabled(true);
    }
}
