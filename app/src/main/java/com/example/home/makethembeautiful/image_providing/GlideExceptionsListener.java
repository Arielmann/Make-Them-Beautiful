package com.example.home.makethembeautiful.image_providing;

import android.util.Log;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by home on 9/6/2016.
 */
public class GlideExceptionsListener implements RequestListener {

    private Object interfacedHolder;

    public GlideExceptionsListener(Object interfacedHolder) {
        this.interfacedHolder = interfacedHolder;
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        Log.e("Glide Error", e.toString());
        OnImageLoadingError error = (OnImageLoadingError) interfacedHolder;
        error.onImageLoadingError();
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
        return false;
    }
}
