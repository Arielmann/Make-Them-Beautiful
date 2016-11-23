package com.example.home.makethembeautiful.app_data_init;

import android.app.ProgressDialog;
import android.content.Context;

import com.crittercism.app.Crittercism;
import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.chat.model.AllConversationsHashMap;
import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersModel;
import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersRowsHashMap;
import com.example.home.makethembeautiful.image_providing.ImageUtils;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

/**
 * Created by home on 9/13/2016.
 */
public class AppDataInit {

/*
* Called when app starts to load data from server and device:
*
* 1. Starts loading chat items from device
* 2. Starts loading conversation rows from device
* 3. Starts initializing user default profile image
* 4. If app runs for the first time, creates app directories
* */

    private static void initDefaultProfileImage(Context context) {
        int imageSizes[] = ImageUtils.chooseImageSizes(context, 2, 2);
        ImageUtils.createImageFromResource(context, R.drawable.female_icon, ImageUtils.defaultProfileImage, imageSizes[0], imageSizes[1]);
    }

    private static void initContactedUsersAdapter(Context context) {
        ContactedUsersModel.getInstance(context).setAdapter(); //Set the adapter for the contacts
    }

    private static void initChatData(Context context) {
        //**************init whole app conversations and contacts data*******//
        if (AllConversationsHashMap.getInstance().getHashMap().isEmpty()) {
            PastChatItemsLoader chatItemsLoader = new PastChatItemsLoader("chatLoadThread", context);
            chatItemsLoader.start(); //It's a thread because expected completion time is MORE then 5 seconds
        }

        if (ContactedUsersRowsHashMap.getInstance().getHashMap().isEmpty()) {
            ContactedUsersRowsLoader loader = new ContactedUsersRowsLoader(context);
            loader.execute();
        }
    }

    public static void createDirectories(){
        Storage storage = SimpleStorage.getExternalStorage();
        storage.createDirectory("Make_Them_Beautiful");
        storage.createDirectory("Make_Them_Beautiful/Contacts");
    }

    public static void initAppData(Context context) {
        //initProfileImageHeader();
        initDefaultProfileImage(context);
        initChatData(context);
        initContactedUsersAdapter(context);
    }


    public static void initAppDataWithProgDialog(final Context context, final Object interfaceHolder) {
        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme);
        initAppData(context);
        //will provoke onImageLoaded OR onImageLoadingError in ToolbarFrag
        ImageUtils.fetchUserProfileImage(context, interfaceHolder);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Initializing");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    public static void initCrashesMonitor(Context context){
        Crittercism.initialize(context, "307f38c6f7684c08a8696859d26443c800555300");
    }
}


