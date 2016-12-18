package com.example.home.makethembeautiful.utils.imageutils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by home on 6/12/2016.
 */
public class BitmapToFileSaver {
    Context context;
    Bitmap bitmap;

    public BitmapToFileSaver(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

  /*  public void saveToFile() throws IOException {
        FileSaveHandler fileSaveHandler = new FileSaveHandler(Activity);
        File file = fileSaveHandler.createImageFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
    }*/
/*
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
