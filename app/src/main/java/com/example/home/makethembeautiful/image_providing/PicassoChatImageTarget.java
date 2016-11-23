package com.example.home.makethembeautiful.image_providing;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by home on 8/24/2016.
 */
public class PicassoChatImageTarget extends PicassoLoadedBitmapHandler implements Target {

    private int counter;
    private String senderName;

    public PicassoChatImageTarget(Context context, ImageLoader interfaceHolder, String senderName, String url) {
        super(context, interfaceHolder, senderName, url);
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        super.handleBitmap(bitmap);
        counter = 0;
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        if (counter > 5) {
            ImageUtils.downloadChatImage(super.getContext(),
                    super.getLoader(),
                    senderName, super.getUrl());
            counter++;
        }
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
};

