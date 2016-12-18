package com.example.home.makethembeautiful.contactedusers.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.contactedusers.events.OnContactedUsersLoadedEvent;
import com.example.home.makethembeautiful.contactedusers.model.ContactedUsersModel;
import com.example.home.makethembeautiful.contactedusers.model.ContactedUsersRow;
import com.example.home.makethembeautiful.contactedusers.model.ContactedUsersRowsHashMap;
import com.example.home.makethembeautiful.dbmanager.DataBaseManager;
import com.example.home.makethembeautiful.utils.imageutils.ImageLoader;
import com.example.home.makethembeautiful.utils.imageutils.ImageUtils;
import com.example.home.makethembeautiful.profile.profilemodels.User;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutionException;

/**
 * Created by home on 7/26/2016.
 */
public class ContactedUsersTableWriter extends SQLiteOpenHelper implements ImageLoader {

    /*
    * The Writer is called when a the user sends
    * his first message to a newly chosen Stylist
    * within the ChatScreen OR when he get's a new
    * message from a stylist. (for now, the app checks if
    * its a new stylist updateConversationRow() method located
    * within the ChatScreen controllers is called every time the user
    * sends any kind of message)
    * */

    private static final String CONTACTED_USERS_TABLE = DataBaseManager.FeedEntry.CONTACTED_USERS_TABLE;
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TOKEN = "gcm_token";
    private Context context; //defined in add contact to table method for saving the user
    private User addressedUser; //defined in add contact to table method for saving the user;
    private String lastMessageDate; //defined in add contact to table method for saving the user;
    private String lastMessage; //defined in add contact to table method for saving the user;

    public ContactedUsersTableWriter(Context context, int version) {
        super(context, DataBaseManager.FeedEntry.DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        getTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTED_USERS_TABLE);
        onCreate(db);
    }

    private void getTable(SQLiteDatabase db) {
        //  db.execSQL("DROP TABLE IF EXISTS " + CONTACTED_USERS_TABLE);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CONTACTED_USERS_TABLE + //TODO: SHOULD BE CALLED IN ONCREATE()!
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "location TEXT, " +
                "image TEXT NOT NULL, " +
                "description TEXT, " +
                "token TEXT NOT NULL)");
    }

    public void addContactToTable(final Context context, final User[] user, final String lastMessageDate, final String lastMessage) {
        this.context = context;
        this.addressedUser = user[0];
        this.lastMessageDate = lastMessageDate;
        this.lastMessage = lastMessage;
        ImageUtils.downloadProfileImage(context, this, user[0], user[0].getProfileImageUrl());
    }

    @Override
    public void onImageLoaded(String senderName, Bitmap scaledBitmap, ChatItem.ItemType itemType, Uri imageUri) throws ExecutionException, InterruptedException {
        //Save user to local DB
        new AsyncTask<User, Void, User>() {
            @Override
            protected User doInBackground(User... userInArray) {
                SQLiteDatabase db = getWritableDatabase(); //TODO: DO WITH AsyncTask
                db.execSQL("CREATE TABLE IF NOT EXISTS " + CONTACTED_USERS_TABLE + //TODO: SHOULD BE CALLED IN ONCREATE()!
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "location TEXT, " +
                        "image TEXT NOT NULL, " +
                        "description TEXT, " +
                        "token TEXT NOT NULL)");

                ContentValues values = new ContentValues();

                try {
                    values.put(KEY_NAME, userInArray[0].getName());
                } catch (NullPointerException e) {
                    throw new ClassCastException(getClass().toString()
                            + "Addressed stylist passed from contactedUsersFrag is NULL!,  ");
                }

                //Create the profile image file on device, based on stylist bitmap
                ImageUtils.setUserImageFile(context,
                        userInArray[0],
                        userInArray[0].getUserImageBitmap(),
                        userInArray[0].getName());

                //DataBaseManager.getInstance(context).setUserImageFile(userInArray[0]);
                values.put(KEY_LOCATION, userInArray[0].getLocation());
                values.put(KEY_IMAGE, userInArray[0].getProfileImagePath());
                values.put(KEY_DESCRIPTION, userInArray[0].getDescription());
                values.put(KEY_TOKEN, userInArray[0].getGcmToken());
                db.insert(CONTACTED_USERS_TABLE, null, values);
                EventBus.getDefault().postSticky(db);
                return userInArray[0];
            }

            @Override
            protected void onPostExecute(User user) {
                //add contact row for ContactedUsesHashMap
                ContactedUsersModel contactedUsersModel = ContactedUsersModel.getInstance(context);
                ContactedUsersRow contactedUserRow = new ContactedUsersRow(user.getName(), user.getProfileImagePath(), lastMessageDate, lastMessage);
                contactedUserRow.setBitmap(user.getUserImageBitmap()); // bitmap is already defined
                ContactedUsersRowsHashMap.getInstance().getHashMap().put(user.getName(), contactedUserRow);
                contactedUsersModel.getDataSet().add(contactedUserRow);
                EventBus.getDefault().post(new OnContactedUsersLoadedEvent(contactedUsersModel.getDataSet()));
                ContactedUsersModel.getInstance(context).getAdapter().notifyDataSetChanged();
                //TODO: find a way to close the db without crashing
            }
        }.execute(addressedUser);
    }
}
