package com.example.home.makethembeautiful.contactedusers.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.home.makethembeautiful.chat.ChatActivity;
import com.example.home.makethembeautiful.dbmanager.DataBaseManager;
import com.example.home.makethembeautiful.profile.profilemodels.User;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by home on 7/27/2016.
 */
public class OnContactedUserClicked implements View.OnClickListener{

    /*
    * This class creates a ChatScreen DIRECTLY.
    * It MUST create a valid User so the
    * new ChatScreen can save his name to the
    * "conversation_name" column in
    * the ChatItemsTable AND ALSO present it's
    * details within the ContactDetailsFrag
    */


    private Context context;
    private String[] addressedUserName;

    public OnContactedUserClicked(Context context, String addressedUserName[]) {
        this.context = context;
        this.addressedUserName = addressedUserName;
    }

    @Override
    public void onClick(View v) {
        User addressedStylist = DataBaseManager.getInstance(context)
                .getContactedStylistsReader().getUser(addressedUserName[0]);

        Intent gotoChatScreen = new Intent(context, ChatActivity.class);
        gotoChatScreen.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        EventBus.getDefault().postSticky(addressedStylist);
        context.startActivity(gotoChatScreen);
    }
}
