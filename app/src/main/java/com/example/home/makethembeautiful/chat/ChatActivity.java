package com.example.home.makethembeautiful.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.home.makethembeautiful.chat.model.MessageItemsFromServer;
import com.example.home.makethembeautiful.utils.handlers.FragmentBuilder;
import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.chat.model.ChatDataModel;
import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.chat.fragments.ChatFrag;
import com.example.home.makethembeautiful.chat.controllers.ChatImagesController;
import com.example.home.makethembeautiful.chat.controllers.ChatTextMessagesController;
import com.example.home.makethembeautiful.utils.imageutils.ChooseImageProvider;
import com.example.home.makethembeautiful.utils.imageutils.ImageLoader;
import com.example.home.makethembeautiful.utils.imageutils.ImageUtils;
import com.example.home.makethembeautiful.utils.imageutils.fragments.FullScreenImageViewFrag;
import com.example.home.makethembeautiful.profile.sharedprefrences.SharedPrefManager;
import com.example.home.makethembeautiful.profile.profilemodels.User;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutionException;

/*
    *  the addressedUserName is ALWAYS the name
    *  of the conversation, thus necessary for
    *  the ChatModel for writing to the database.

   The Activity is being called in two different scenarios:

    * 1. From StylistDetailsScreen: The user
    * chose a new Stylist to connect with.
    *
    * 2. From OnContactedUserClicked: The user reloaded
    * an old conversation from the contactedUsers history.
    *
    * 3. from GcmIntentService when a message was received from GcmReceiver
    *
    * The addressedUser will be provided
    * by the event bus
*/

public class ChatActivity extends AppCompatActivity implements OnTextTransferred, ImageLoader, ChooseImageProvider {
    private static final String TAG = "Chat screen";
    private ChatFrag chatPresenter;
    private ChatTextMessagesController chatTextMessagesController;
    private ChatImagesController chatImagesController;
    private User addressedUser;
    private ChatDataModel model;
    private String CHAT_SCREEN_TAG = "Chat Screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initActivity();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initActivity();
    }

    private void initActivity() {
        addressedUser = EventBus.getDefault().removeStickyEvent(User.class);
        model = new ChatDataModel(this, addressedUser);
        chatTextMessagesController = new ChatTextMessagesController(model);
        chatImagesController = new ChatImagesController(model);
        chatPresenter = (ChatFrag) getSupportFragmentManager().findFragmentById(R.id.messagesPresenterFrag);
        chatPresenter.setFragData(model, chatTextMessagesController);
        chatPresenter.scrollChatToBottom(model);
        MessageItemsFromServer.getInstance().getMessageItems().clear();
    }

    @Override
    public void onTextSentFromUser(String senderName, String message) {
        chatTextMessagesController.presentChatItemsOnScreen(senderName, null, null, message, ChatItem.ItemType.TEXT_RIGHT);
        chatPresenter.scrollChatToBottom(model);
        chatTextMessagesController.sendPushNotificationToServer(message);
    }

    @Override
    public void onImageLoaded(String senderName, Bitmap scaledBitmap, ChatItem.ItemType itemType, Uri originalImageUri) throws ExecutionException, InterruptedException {
        Uri finalImageUri = ImageUtils.createImageUri(this, scaledBitmap); //send uri of the final ROTATED image
        chatImagesController.presentChatItemsOnScreen(senderName, scaledBitmap, originalImageUri, null, itemType);
        String thisUserName = SharedPrefManager.getInstance(this).getUserName();
        if (thisUserName.equals(senderName)) { //if image is sent from this user
            chatImagesController.uploadChatImageToServer(this, finalImageUri);
        }
        chatPresenter.scrollChatToBottom(model);
    }

    @Override
    public void onImageProviderChosen(Fragment imageResolverFrag, String fragTag) {
        FragmentBuilder fragmentBuilder = new FragmentBuilder(this);
        fragmentBuilder.buildFrag(imageResolverFrag, fragTag);
    }

    @Override
    public void onBackPressed() {
        //before going to previous activity, check
        //if chat is simply in full image view mode

        FullScreenImageViewFrag fullScreenImageFrag = (FullScreenImageViewFrag) getSupportFragmentManager().findFragmentByTag("fullScreenImageViewFrag");
        if (fullScreenImageFrag != null) {
            Log.d(TAG, "Closing full screen image fragment");
            getSupportFragmentManager().beginTransaction().remove(fullScreenImageFrag).commit();
        } else {
            Log.d(TAG, "Closing chat activity");
            EventBus.getDefault().postSticky(addressedUser);
            super.onBackPressed();
        }
    }
}