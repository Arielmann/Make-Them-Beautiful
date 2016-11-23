package com.example.home.makethembeautiful.image_providing;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.user_profile.profile_objects.User;

import java.util.concurrent.ExecutionException;

/**
 * Created by home on 9/30/2016.
 */
public abstract class PicassoLoadedBitmapHandler {

      /*
    * This abstract class is inherited by chat and profile
    * image picasso targets. it saves code copying and
    * allows it's sons to retry loading the image upon error
    * */

    private String senderName;
    private ImageLoader loader;
    private User user;
    private Context context;
    private String url;

    //Load profile image
    public PicassoLoadedBitmapHandler(Context context, ImageLoader interfaceHolder, User user, String url) {
        this.context = context;
        this.user = user;
        this.loader = interfaceHolder;
        this.url = url;
    }

    //Load image for chat item
    public PicassoLoadedBitmapHandler(Context context, ImageLoader interfaceHolder, String senderName, String url) {
        this.context = context;
        this.senderName = senderName;
        this.loader = interfaceHolder;
        this.url = url;
    }

       /*
    * There are 2 scenarios to handle a downloaded bitmap:
    *
    * 1. set it as the stylist's profile
    * image (targeted stylist is transferred as reference, no
    * interface required)
    *
    * 2. activate interface implemented in the
    * ChatScreen for further treatment (no stylist object required)
    * */


    protected void handleBitmap(Bitmap bitmap){
        Log.d("Loaded Bitmap Handler", "image loaded");
        int[] imageSizes = ImageUtils.chooseImageSizes(context, 2, 2);
        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, imageSizes[1], imageSizes[0], true);
        Log.d("Loaded Bitmap Handler", "final image created");
        ImageLoader loader = this.loader;
        try {
            loader.onImageLoaded(senderName, finalBitmap, ChatItem.ItemType.IMAGE_LEFT, null);
            Log.d("Loaded Bitmap Handler", "interface was activated, bitmap was sent for setting to user");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("Loaded Bitmap Handler", "Interface is null, image is downloaded from ReadingStylists AsyncTask");
        }try {
            user.setUserImageBitmap(finalBitmap);
        }catch (NullPointerException e){
            Log.e("Loaded Bitmap Handler", "user is null, chat item is handled. no need to set profile image");
        }
    }

    public ImageLoader getLoader() {
        return loader;
    }

    public User getUser() {
        return user;
    }

    public Context getContext() {
        return context;
    }

    public String getUrl() {
        return url;
    }
}
