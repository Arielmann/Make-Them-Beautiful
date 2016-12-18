package com.example.home.makethembeautiful.contactedusers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.appinit.AppDataInit;
import com.example.home.makethembeautiful.contactedusers.fragments.ContactedUsersPresenterFrag;
import com.example.home.makethembeautiful.utils.handlers.GoToScreen;
import com.example.home.makethembeautiful.utils.handlers.FragmentBuilder;
import com.example.home.makethembeautiful.toolbar.ToolbarFrag;
import com.example.home.makethembeautiful.profile.sharedprefrences.SharedPrefManager;
import com.example.home.makethembeautiful.profile.registration.basic.RegisterBasicProfileActivity;

public class ContactedUsersActivity extends AppCompatActivity{
    private static final String CONTACTED_USERS_SCREEN = "Contacted users screen";

    /* will manage the whole process of
    presenting the conversations that the user created

    * Also initializing app data for it is its main activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacted_users);
        AppDataInit.initCrashesMonitor(this);
        FragmentBuilder builder = new FragmentBuilder(this);
        builder.buildFrag(R.id.contactedUsersInnerRelativeLayout, new ContactedUsersPresenterFrag(), "Contacted Users Presenter");
        ToolbarFrag toolbarFrag = (ToolbarFrag) getSupportFragmentManager().findFragmentById(R.id.toolbarFragInWelcomeScreen);
        //Check if registration is required
        if (SharedPrefManager.getInstance(this).getUserName().equals("user name error")) {
            goToRegistrationScreen();
        } else {
            Log.d(CONTACTED_USERS_SCREEN, SharedPrefManager.getInstance(this).toString());
            AppDataInit.initAppDataWithProgDialog(this, toolbarFrag);
        }
    }

    private void goToRegistrationScreen() {
        GoToScreen goToRegistrationBasicProfileScreen = new GoToScreen(this, RegisterBasicProfileActivity.class);
        goToRegistrationBasicProfileScreen.onClick(null);
    }
}
