package com.example.home.makethembeautiful.chat.controllers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.home.makethembeautiful.chat.model.ChatDataModel;
import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.contactedusers.model.ContactedUsersModel;
import com.example.home.makethembeautiful.contactedusers.model.ContactedUsersRowsHashMap;
import com.example.home.makethembeautiful.imageutils.ImageUtils;
import com.example.home.makethembeautiful.servercommunication.OnImageUploadedToServer;
import com.example.home.makethembeautiful.servercommunication.SendMessagePushNotification;
import com.example.home.makethembeautiful.servercommunication.UploadData;
import com.example.home.makethembeautiful.profile.sharedprefrences.SharedPrefManager;
import com.example.home.makethembeautiful.profile.profilemodels.User;

/**
 * Created by home on 7/8/2016.
 */

public class ChatImagesController extends IChatController implements OnImageUploadedToServer {

    /*
    * NOTE: this class checks every time if the
    * sent ChatItem is the first message of this user
    * (by checking if it exists in the database).
    * if it's true (trying to retrieve the contact returns null),
    * it will save it in the database
    * */
    //TODO: improve the mentioned above method, should be ONE PURPOSE

    private ChatDataModel model;
    private static final String TAG = "Chat images controller";

    public ChatImagesController(ChatDataModel model) {
        this.model = model;
    }

    private ChatItem addChatImageToArray(String senderName, Bitmap scaledImage, String imagePath, ChatItem.ItemType itemType) {
        ChatItem chatItem = new ChatItem(senderName, scaledImage, imagePath, itemType, "Photo from " + senderName);
        model.getChatItems().add(chatItem);
        return chatItem;
    }

    @Override
    public void saveAddressedUserToTable(ChatItem item) {
        //if the contact user doesn't exists on the db (his name == null), save it.
        String addressedUserName = model.getAddressedUser().getName();
        boolean userIsInSingleTon= ContactedUsersRowsHashMap.getInstance().userIsInDataBase(model.getContext(), addressedUserName);
        if (userIsInSingleTon) {
            //IMPORTANT: user MUST be in contacts Singleton at this point
            ContactedUsersRowsHashMap.getInstance().getHashMap().get(addressedUserName).setLastMessageAsText(item);
            ContactedUsersRowsHashMap.getInstance().getHashMap().get(addressedUserName).setLastMessageDate(item);
            ContactedUsersModel.getInstance(model.getContext()).initDataSet();
            ContactedUsersModel.getInstance(model.getContext()).getAdapter().notifyDataSetChanged();

        } else {
            User[] userInArray = new User[]{model.getAddressedUser()};
            model.getUsersTableWriter().addContactToTable(model.getContext(), userInArray, item.getMessageDate(), item.getTextMessage());
        }
    }


    @Override
    public void saveChatItemInTable(ChatItem chatItem, Uri imageUri) {
        final String imagePath = ImageUtils.getRealPathFromURI(model.getContext(), imageUri);
        chatItem.setImagePath(imagePath);
        model.getChatItemsTable().saveChatItemInTable(chatItem, model.getAddressedUser().getName());
    }


    @Override
    public void presentChatItemsOnScreen(String senderName, Bitmap bitmap, Uri imageUri, String message, ChatItem.ItemType itemType) {
        String imagePath = ImageUtils.getRealPathFromURI(model.getContext(), imageUri);
        ChatItem chatItem = addChatImageToArray(senderName, bitmap, imagePath, itemType); //The original image uri before glide manipulations will be saved to DB
        model.getAdapter().notifyDataSetChanged();
        saveChatItemInTable(chatItem, imageUri);
        saveAddressedUserToTable(chatItem); //AsyncTask executed here
    }

    public void uploadChatImageToServer(Activity activity, Uri imageUriForUploading) {
        Log.d(TAG, "start uploading image method");
        String imagePath = ImageUtils.getRealPathFromURI(activity, imageUriForUploading);
        UploadData imageUploader = new UploadData(activity, this, imagePath, imageUriForUploading);
        imageUploader.execute();
        Log.d(TAG, "image uploader executed");
    }

    @Override
    public void handleServerImageUrl(String imageUrl, String imagePath, Uri rotatedImageForDelete) {
        ImageUtils.deleteImage(model.getContext(), rotatedImageForDelete);
        String userName = SharedPrefManager.getInstance(model.getContext()).getUserName();
        SendMessagePushNotification pushNotification = new SendMessagePushNotification(model.getContext(), model.getAddressedUser().getGcmToken(), imageUrl,  "Photo from " + userName);
        pushNotification.execute();
    }
}

