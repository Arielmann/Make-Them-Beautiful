package com.example.home.makethembeautiful.utils.handlers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import com.example.home.makethembeautiful.R;

/**
 * Created by home on 4/13/2016.
 */
public class GoToScreen implements View.OnClickListener {

    public Intent getIntent() {
        return goToScreen;
    }

    private Activity activity;
    private Intent goToScreen;
    private String dialogMessage;

    public GoToScreen(Activity activity, Class screen, String dialogMessage) {
        this.activity = activity;
        this.goToScreen = new Intent(activity, screen);
        this.dialogMessage = dialogMessage;
    }

    public GoToScreen(Activity activity, Class screen) {
        this.activity = activity;
        this.goToScreen = new Intent(activity, screen);
    }

    @Override
    public void onClick(View v) {
        activity.startActivity(goToScreen);
    }

    //Method is never used but might be useful in the future
    public View.OnClickListener goToScreenWithProgressDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressDialog progressDialog = new ProgressDialog(activity,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            if (dialogMessage != null) {
                progressDialog.setMessage(dialogMessage);
            }
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            activity.startActivity(goToScreen);
                        }
                    }, 2000);
        }
    };
}


