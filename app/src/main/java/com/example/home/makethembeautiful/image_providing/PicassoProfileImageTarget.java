package com.example.home.makethembeautiful.image_providing;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.home.makethembeautiful.user_profile.profile_objects.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by home on 8/24/2016.
 */
public class PicassoProfileImageTarget extends PicassoLoadedBitmapHandler implements Target {

    private int counter;

    public PicassoProfileImageTarget(Context context, ImageLoader interfaceHolder, User user, String url) {
        super(context, interfaceHolder, user, url);
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        super.handleBitmap(bitmap);
        counter = 0;
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        if (counter > 5) {
            ImageUtils.downloadProfileImage(super.getContext(),
                    super.getLoader(),
                    super.getUser(),
                    super.getUrl());
            counter++;
        }
    }


    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
};

