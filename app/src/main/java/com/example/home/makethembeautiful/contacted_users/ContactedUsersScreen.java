package com.example.home.makethembeautiful.contacted_users;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.app_data_init.AppDataInit;
import com.example.home.makethembeautiful.contacted_users.events.OnContactedUsersLoadedEvent;
import com.example.home.makethembeautiful.contacted_users.fragments.ContactedUsersPresenterFrag;
import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersModel;
import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersRow;
import com.example.home.makethembeautiful.go_to_screen.GoToScreen;
import com.example.home.makethembeautiful.handlers.FragmentBuilder;
import com.example.home.makethembeautiful.toolbar_frag.ToolbarFrag;
import com.example.home.makethembeautiful.user_profile.SharedPrefrences.SharedPrefManager;
import com.example.home.makethembeautiful.user_profile.user_info.basic_info.RegisterBasicProfileScreen;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ContactedUsersScreen extends AppCompatActivity{
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
        GoToScreen goToRegistrationBasicProfileScreen = new GoToScreen(this, RegisterBasicProfileScreen.class);
        goToRegistrationBasicProfileScreen.onClick(null);
    }
}
