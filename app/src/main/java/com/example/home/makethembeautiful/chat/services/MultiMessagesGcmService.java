package com.example.home.makethembeautiful.chat.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.appinit.AppDataInit;
import com.example.home.makethembeautiful.chat.ChatActivity;
import com.example.home.makethembeautiful.chat.controllers.ChatImagesController;
import com.example.home.makethembeautiful.chat.controllers.ChatTextMessagesController;
import com.example.home.makethembeautiful.chat.model.ChatDataModel;
import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.chat.model.MessageItemsFromServer;
import com.example.home.makethembeautiful.utils.imageutils.ImageLoader;
import com.example.home.makethembeautiful.utils.imageutils.ImageUtils;
import com.example.home.makethembeautiful.profile.sharedprefrences.SharedPrefManager;
import com.example.home.makethembeautiful.profile.profilemodels.User;
import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

/**
 * Created by home on 10/4/2016.
 */

/*
* NOTE: This class is not yet in use
*/

public class MultiMessagesGcmService extends IntentService implements ImageLoader {

    private static final String MESSAGES_KEY = "messages";
    private static final String TAG = "multiple msg service";
    private User addressedUser;
    private String senderName;
    private String textMessage; //TODO: make arraylist for multiple notifications
    private String imageUrlMessage; //TODO: make arraylist for multiple notifications
    private ChatDataModel model;
    private ChatImagesController imagesController;
    private ChatTextMessagesController textMessagesController;
    private NotificationCompat.InboxStyle inbox;
    private List<String> messages = MessageItemsFromServer.getInstance().getMessageItems();
    private Handler serviceHandler;


    public MultiMessagesGcmService() {
        super("MultiMessagesGcmService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        //Contacted users has to be set in case the app is being created from here
        Log.d(TAG, "intent handling started");
        inbox = new NotificationCompat.InboxStyle();
        serviceHandler = new Handler(Looper.getMainLooper());
        serviceHandler.post(new Runnable() {
            @Override
            public void run() {
                AppDataInit.initAppData(getApplicationContext());
 //*****************************************************************************//

                senderName = intent.getStringExtra("sender_name");
                textMessage = intent.getStringExtra("msg_text");
                imageUrlMessage = intent.getStringExtra("msg_image_url");

                initUserFromServerBundle(intent);
                sendPushNotification(intent);
                Log.d(TAG, "push notification is displayed");
            }
        });
    }

//******************************Class Logic Here**********************************************//

    private void sendPushNotification(Intent dataIntent) {
        Bundle extras = dataIntent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(dataIntent);

        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            displayNotificationOnScreen(getApplicationContext(), initDestinationScreenIntent(), dataIntent.getStringExtra("msg_text"));
        }
        GcmReceiver.completeWakefulIntent(dataIntent);
    }

    private void initUserFromServerBundle(Intent dataIntent) {
        String senderName = dataIntent.getStringExtra("sender_name");
        String location = dataIntent.getStringExtra("location");
        String profileImageUrl = "no image url (string defined in chat service)";
        if (dataIntent.getStringExtra("profile_image_url") != null) {
            profileImageUrl = dataIntent.getStringExtra("profile_image_url");
        }
        String description = dataIntent.getStringExtra("description");
        String gcmToken = dataIntent.getStringExtra("sender_reg_id");
        addressedUser = new User(1, senderName, location, profileImageUrl, description, gcmToken);
        model = new ChatDataModel(getApplicationContext(), addressedUser);
        imagesController = new ChatImagesController(model);
        textMessagesController = new ChatTextMessagesController(model);
        handleMessageItems();
        EventBus.getDefault().postSticky(addressedUser);
    }

    private void displayNotificationOnScreen(Context context, Intent goToScreen, String message) {
        addNewLineToNotiInbox();
        Log.d(TAG, "started notification displaying method");
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, goToScreen, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.thumb_up_white_18dp)
                .setContentTitle(senderName)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(inbox)
                .setGroup(MESSAGES_KEY) //TODO: use it to receive multiple notifications
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        //generate unique notification id for multiple notifications display
        int notificationId = SharedPrefManager.getInstance(context).getNotificationsCounter();
        notificationManager.notify(1, notification);
    }

    private void addNewLineToNotiInbox() {
        for (String message : messages) {
            inbox.addLine(message);
        }
        if (messages.size() > 1) {
            inbox.setBigContentTitle(messages.size() + " new messages");
        }
    }

    private Intent initDestinationScreenIntent() {
        Intent goToChatScreen = new Intent(this, ChatActivity.class);
        Log.d(TAG, "Addressed user built: " + addressedUser.toString());
        goToChatScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return goToChatScreen;
    }

    private void handleMessageItems() {
        if (!imageUrlMessage.equals("-1")) { //if image is not error
            messages.add(addressedUser.getName() + ": Photo");
            final ImageLoader loader = this;
            ImageUtils.downloadChatImage(getApplicationContext(), loader, addressedUser.getName(), imageUrlMessage);
            return;
        }
        if (textMessage != null) {
            messages.add(addressedUser.getName() + ": " + textMessage);
            textMessagesController.presentChatItemsOnScreen(addressedUser.getName(), null, null, textMessage, ChatItem.ItemType.TEXT_LEFT);
        }
    }

    @Override
    public void onImageLoaded(String senderName, Bitmap scaledBitmap, ChatItem.ItemType itemType, Uri imageUri) {
        Uri finalImageUri = ImageUtils.createImageUri(this, scaledBitmap); //send uri of the final ROTATED image
        String path = ImageUtils.getRealPathFromURI(getApplicationContext(), finalImageUri);
        File imageFile = new File(path);
        Log.d(TAG, "image file size: " + imageFile.length());
        imagesController.presentChatItemsOnScreen(senderName, scaledBitmap, finalImageUri, null, ChatItem.ItemType.IMAGE_LEFT);
    }
}



