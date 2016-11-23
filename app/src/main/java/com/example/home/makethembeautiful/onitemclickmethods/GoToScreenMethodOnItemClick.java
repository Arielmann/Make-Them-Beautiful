package com.example.home.makethembeautiful.onitemclickmethods;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by home on 4/14/2016.
 */
public class GoToScreenMethodOnItemClick implements AdapterView.OnItemClickListener {
    private Intent goToScreen;
    private Activity activity;

    public GoToScreenMethodOnItemClick(Activity activity, Class screen) {
        this.activity = activity;
        this.goToScreen = new Intent(activity, screen);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        goToScreen.putExtra("ClickedItemPosition", position);
        activity.startActivity(goToScreen);
    }
}
