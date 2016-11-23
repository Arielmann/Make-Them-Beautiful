package com.example.home.makethembeautiful.user_profile.user_info.user_profile_image;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.app_data_init.AppDataInit;
import com.example.home.makethembeautiful.contacted_users.ContactedUsersScreen;
import com.example.home.makethembeautiful.go_to_screen.GoToScreen;
import com.example.home.makethembeautiful.image_providing.ImageUtils;
import com.example.home.makethembeautiful.image_providing.fragments.Dialogs.ChooseImageSourceDialog;
import com.example.home.makethembeautiful.server_communication.OnImageUploadedToServer;
import com.example.home.makethembeautiful.server_communication.OnPushNotificationSent;
import com.example.home.makethembeautiful.server_communication.SaveStylistToServerPushNotification;
import com.example.home.makethembeautiful.server_communication.UploadData;
import com.example.home.makethembeautiful.user_profile.SharedPrefrences.SharedPrefManager;

import java.util.concurrent.ExecutionException;

public class SetProfileImageModel implements View.OnClickListener, OnImageUploadedToServer, OnPushNotificationSent {

     /*
    * This model handels the saving of the user's profile image
    * */

    private final Activity activity;
    private String profileImageFilePathForLocalSaving = "Profile image is unset";
    private String profileImagePathForUploading = "Profile image is unset";
    private final ChooseImageSourceDialog choicesDialog;
    private final String alertBoxTitle = "Choose Profile Picture!";
    ProgressDialog progressDialog;
    private Uri profileImageUriForUploading;

    public SetProfileImageModel(Activity activity) {
        this.activity = activity;
        choicesDialog = new ChooseImageSourceDialog(this.activity, alertBoxTitle, ImageUtils.chooseImageAlertBoxItems);
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        choicesDialog.chooseImageSource();
        v.setEnabled(true);
    }

    //****User has chosen an image. Save it to sharePref and go to welcomeScreen******
    protected View.OnClickListener saveImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Progress dialog will appear until server finish uploading
            progressDialog = new ProgressDialog(activity,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Saving Data");
            progressDialog.show();

            //Save path to Shared Pref
            SharedPrefManager.getInstance(activity).saveStringInfoToSharedPreferences(activity, "profileImageFilePath", profileImageFilePathForLocalSaving);
            try {
                uploadProfileImage(); //FIXME: should not happen here but in the next screen
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void uploadProfileImage() throws ExecutionException, InterruptedException {
        UploadData profileImageUploader = new UploadData(activity, this, profileImagePathForUploading, profileImageUriForUploading);
        profileImageUploader.execute();
    }

    @Override
    public void handleServerImageUrl(String profileImageUrl, String profileImageFilePath, Uri rotatedImageForDelete) {
        // ImageUtils.retryImageUploading(activity, this, profileImageUrl, profileImageFilePath);
        ImageUtils.deleteImage(activity, rotatedImageForDelete);
        SharedPrefManager.getInstance(activity).saveStringInfoToSharedPreferences(activity, "profileImageUrl", profileImageUrl);
        saveUserToServer();
    }

    private void saveUserToServer() {
        SaveStylistToServerPushNotification saveUserToServer = new SaveStylistToServerPushNotification(activity, this);
        saveUserToServer.execute();
    }

    @Override
    public void onPushSuccess() {
        progressDialog.dismiss();
        final GoToScreen goToContactedUsersScreen = new GoToScreen((Activity) activity, ContactedUsersScreen.class);
        goToContactedUsersScreen.onClick(null);
    }

    @Override
    public void onPushFailure() {
        progressDialog.dismiss();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Oops, something went wrong, please try again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setProfileImageUriForUploading(Uri profileImageUriForUploading) {
        this.profileImageUriForUploading = profileImageUriForUploading;
    }

    public void setProfileImageFilePathForLocalSaving(String profileImageFilePathForLocalSaving) {
        this.profileImageFilePathForLocalSaving = profileImageFilePathForLocalSaving;
    }


    public void setProfileImagePathForUploading(String profileImagePathForUploading) {
        this.profileImagePathForUploading = profileImagePathForUploading;
    }
}
