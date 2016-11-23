package com.example.home.makethembeautiful.image_providing.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.home.makethembeautiful.image_providing.ImageUtils;
import com.example.home.makethembeautiful.user_profile.SharedPrefrences.SharedPrefManager;

import java.io.Serializable;

/**
 * Created by home on 4/24/2016.
 */
public class TakePhotoFrag extends Fragment implements Serializable{
    private static final int CAM_REQUEST = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(takePictureIntent, CAM_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //****The actual camera inbuilt application opens here****///
        try {
            if (requestCode == CAM_REQUEST && resultCode == getActivity().RESULT_OK) {
                Uri selectedImageUri = data.getData();
                String senderName = SharedPrefManager.getInstance(getContext()).getUserName();
                int[] imageSizes =   ImageUtils.chooseImageSizes(getActivity(), 2, 2);
                ImageUtils.createBitmapFromImageSource(senderName, getActivity(), selectedImageUri, imageSizes[0], imageSizes[1]);
                return;
                //Adding Image to array method occurs on parent class

            } else if (requestCode == CAM_REQUEST && resultCode == getActivity().RESULT_CANCELED) {


                return;
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    }








