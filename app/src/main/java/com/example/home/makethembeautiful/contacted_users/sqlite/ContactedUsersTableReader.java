package com.example.home.makethembeautiful.contacted_users.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.home.makethembeautiful.chat.model.AllConversationsHashMap;
import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersRow;
import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersRowsHashMap;
import com.example.home.makethembeautiful.db_manager.DataBaseManager;
import com.example.home.makethembeautiful.user_profile.profile_objects.User;

import java.util.List;

/**
 * Created by home on 7/26/2016.
 */
public class ContactedUsersTableReader extends SQLiteOpenHelper {

    /*
    * Reader class is used throughout various scenarios
    * in the application:
    *
    * 1. when the User clicks a ContactedUserConversationRow
    * and loads the AddressedUser details from the old Conversation
    *
    * 2. When the ContactedUsersLoader loads the entire ContactedUsersArray
    * when the application is initialized.
    *
    * 3. When the ChatScreen's controllers has to determine if the
    * Contacted User was ever contacted by the the app's User
    * in the past
    * */

    public ContactedUsersTableReader(Context context, int version) {
        super(context, DataBaseManager.FeedEntry.DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        getTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + CONTACTED_USERS_TABLE);
        onCreate(db);

    }

    // Contacts table name
    private static final String CONTACTED_USERS_TABLE = DataBaseManager.FeedEntry.CONTACTED_USERS_TABLE;
    private static final String CHAT_ITEMS_TABLE = DataBaseManager.FeedEntry.CHAT_ITEMS_TABLE;

    // Contacts Table Columns names

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TOKEN = "gcm_token";

    //***************************************************************************************************//
    private void getTable(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + CONTACTED_USERS_TABLE);
        String CREATE_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACTED_USERS_TABLE +
                " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT NOT NULL, " +
                KEY_LOCATION + " TEXT NOT NULL, " +
                KEY_IMAGE + " TEXT NOT NULL, " +
                KEY_DESCRIPTION + " TEXT NOT NULL, " +
                KEY_TOKEN + " TEXT NOT NULL)";

        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    //***************************************************************************************************//
    public ContactedUsersRow getContactConversationRow(final String name) {
        SQLiteDatabase db = getReadableDatabase();
        getTable(db);
        Cursor cursor = db.query(CONTACTED_USERS_TABLE,
                new String[]{KEY_IMAGE}, KEY_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ContactedUsersRow contactedUserConversationRow;
        try {
            String ContactImage = cursor.getString(0);
            contactedUserConversationRow = new ContactedUsersRow(name,
                    ContactImage, "Yesterday", "Last message");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return contactedUserConversationRow;
    }


    //***************************************************************************************************//
    public void initAllContactsInSingleton() {
        SQLiteDatabase db = getReadableDatabase();
        getTable(db);

        // Select All Query
        String selectContactsQuery = "SELECT name,image FROM " + CONTACTED_USERS_TABLE; //TODO: don't reat the item id if you don't use it
        Cursor nameAndImageCursor = db.rawQuery(selectContactsQuery, null);

        // looping through all rows and adding to list
        if (nameAndImageCursor.moveToFirst()) {
            do {
                String name = nameAndImageCursor.getString(0);
                String image = nameAndImageCursor.getString(1);
                String lastMessageDate = "Last message date";
                String lastMessage = "Last message";


                List<ChatItem> contactedUserConv = AllConversationsHashMap.getInstance().getHashMap().get(name);

                if (contactedUserConv != null) {
                    lastMessageDate = contactedUserConv.get(contactedUserConv.size() - 1).getMessageDate();
                    ChatItem lastChatItem = contactedUserConv.get(contactedUserConv.size() - 1);
                    lastMessage = createMessageTextOrFilePath(lastChatItem);
                } else {
                    String selectMessagesQuery = "SELECT sender_name,text_message,file_path,message_date " +
                            "FROM " + CHAT_ITEMS_TABLE +
                            " WHERE conversation_name = '" + name + "' ORDER BY message_date DESC LIMIT 1";
                    Cursor messagesCursor = db.rawQuery(selectMessagesQuery, null);
                    if (messagesCursor.moveToFirst()) {
                        lastMessage = createMessageTextOrFilePath(messagesCursor.getString(0),messagesCursor.getString(1), messagesCursor.getString(2));
                        lastMessageDate = messagesCursor.getString(3);
                    }
                }

                ContactedUsersRow contactedUserRow = new ContactedUsersRow(name, image, lastMessageDate, lastMessage);

                // Adding stylist to list
                ContactedUsersRowsHashMap.getInstance().getHashMap().put(name, contactedUserRow);
                Log.d("Contacted Users Reader", "row added to singleton: " + contactedUserRow.toString());
            } while (nameAndImageCursor.moveToNext());
        }
    }

    //***************************************************************************************************//
    public User getUser(String name) {
        SQLiteDatabase db = getReadableDatabase();
        getTable(db);
        Cursor cursor = db.query(CONTACTED_USERS_TABLE, new String[]{KEY_ID, KEY_NAME,
                        KEY_LOCATION, KEY_IMAGE,
                        KEY_DESCRIPTION, KEY_TOKEN}, KEY_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5));
            // return contact
            return user;
        }
        return new User(1, "defined in contacts reader", "location", "url", "description", "token");
    }

    //***************************************************************************************************//
    //ChatItem is provided from AllConversationsArray
    private String createMessageTextOrFilePath(ChatItem item) {
        if (item.getImagePath() != null) {
            return "Photo from " + item.getSenderName();
        } else {
            return item.getTextMessage();
        }
    }

    //message and image are provided from DB
    private String createMessageTextOrFilePath(String senderName, String textMessage, String filePath) {
        if (textMessage != null) {
            return textMessage;
        } else {
            return "Photo from " + senderName;
        }
    }
}
