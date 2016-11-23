package com.example.home.makethembeautiful.user_profile.user_info.user_profile_image;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.home.makethembeautiful.handlers.FragmentBuilder;
import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.image_providing.ChooseImageProvider;
import com.example.home.makethembeautiful.image_providing.ImageLoader;
import com.example.home.makethembeautiful.image_providing.ImageUtils;


public class SetProfileImageScreen extends AppCompatActivity implements ChooseImageProvider, ImageLoader {

    private SetProfileImageViewFrag imagePresenterFrag;
    private FragmentBuilder fragmentBuilder;  //FragmentBuilder is a LOCAL class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_image);
        imagePresenterFrag = (SetProfileImageViewFrag) getSupportFragmentManager().findFragmentById(R.id.setProfileImageFrag);
        fragmentBuilder = new FragmentBuilder(this);

    }

    @Override
    public void onImageProviderChosen(Fragment imageResolverFrag, String fragTag) {
       fragmentBuilder.buildFrag(imageResolverFrag, fragTag);
    }

    @Override
    public void onImageLoaded(String senderName, Bitmap scaledBitmap, ChatItem.ItemType itemType, Uri uriForLocalImageSaving) {
        Uri uriForImageUploading = ImageUtils.createImageUri(this, scaledBitmap); //send uri of the final ROTATED image
        imagePresenterFrag.presentingImageView.setImageBitmap(scaledBitmap);
        imagePresenterFrag.saveImageDetails(uriForImageUploading, uriForLocalImageSaving);
    }
}



