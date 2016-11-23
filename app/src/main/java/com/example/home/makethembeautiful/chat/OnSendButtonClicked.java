package com.example.home.makethembeautiful.chat;

import android.content.Context;
import android.view.View;

/**
 * Created by home on 5/8/2016.
 */
public class OnSendButtonClicked implements View.OnClickListener {

    /*
    * This class is responsible to provoke the upload process
    * once the send button was clicked in the conversation screen.
    *
    * Since image file saving into device database should occur only when
    * the user decided to send a message, this class also provokes
    * the FileSaveHandler class
    *
    * it also provokes the SQLite savers that stores the images
    * and messages of the user on his device for later usage
    *
    * This class is will eventually be able to send images OR messages OR both
    * (for now sends messages only)
    * */

    private Context context;


    public OnSendButtonClicked(Context context) {
        this.context = context;
        //onSendButtonClicked = (ChatScreen) this.context; //Should somehow apply to sent imges as well
    }


    @Override
    public void onClick(View v) { //provokes method in chatScreen

     /*   //        for (int i = 0; i < filePathsArray.size() - 1; i++)
        UploadData uploadData = new UploadData(context, FilePathsArray.getInstance().getArray().get(0)); //upload data should be created once and make asynctasks
        uploadData.execute();
        SqliteFileTable sqliteFileTable = new SqliteFileTable(context, 3);
        sqliteFileTable.addFile(FilePathsArray.getInstance().getArray().get(0)); */
    }
}
//    File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);//FileSaveHandler.getArray().getImageFiles().get(0);
//    File camera = new File(f, "Camera");
//    File image = camera.listFiles()[1];
