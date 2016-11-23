package com.example.home.makethembeautiful.user_profile.user_info.company_description;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.user_profile.user_info.GenericSettingsFrag;
import com.example.home.makethembeautiful.user_profile.user_info.SetViewsAfterSignUpClicked;

import java.util.HashMap;

/**
 * Created by home on 8/5/2016.
 */
public class SetDescriptionViewFrag extends GenericSettingsFrag implements View.OnClickListener, SetViewsAfterSignUpClicked {
    private EditText descriptionEditText;
    private Button saveDetailsButton;

    @Override
    public void tryToUpdateData() {
        HashMap<String, String> userInputs = new HashMap<>();
        userInputs.put("description", descriptionEditText.getText().toString());
        getModel().validate(userInputs);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View setDescriptionLayout = inflater.inflate(R.layout.frag_register_description, null);
        descriptionEditText = (EditText) setDescriptionLayout.findViewById(R.id.input_description);
        saveDetailsButton = (Button) setDescriptionLayout.findViewById(R.id.btn_saveDescription);
        setModel(new SetDescriptionModel(this));
        saveDetailsButton.setOnClickListener(this);
        return setDescriptionLayout;
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
        descriptionEditText.setError(errors.get("descriptionError"));
        saveDetailsButton.setEnabled(true);
    }
}

