package com.example.home.makethembeautiful.image_providing;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.handlers.FragmentBuilder;
import com.example.home.makethembeautiful.image_providing.fragments.FullScreenImageViewFrag;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by home on 10/17/2016.
 */
public class OnImageClickedListener implements View.OnClickListener {

    private Context context;
    private Bitmap bitmap;

    public OnImageClickedListener(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().postSticky(bitmap);
        //Create full screen image fragment. Base activity is ChatScreen
        FragmentBuilder fragmentBuilder = new FragmentBuilder(context);
        fragmentBuilder.buildFrag(R.id.chatFrameLayout, new FullScreenImageViewFrag(), "fullScreenImageViewFrag");
    }
}
