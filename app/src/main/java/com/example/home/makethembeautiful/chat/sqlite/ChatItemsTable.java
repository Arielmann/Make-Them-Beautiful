package com.example.home.makethembeautiful.chat.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.home.makethembeautiful.chat.model.AllConversationsHashMap;
import com.example.home.makethembeautiful.chat.model.ChatItem;
import com.example.home.makethembeautiful.dbmanager.DataBaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 7/1/2016.
 */
public class ChatItemsTable extends SQLiteOpenHelper {
    //This class saves all chat items in one table
    private Context context;
    private final String CHAT_ITEMS_TABLE = DataBaseManager.FeedEntry.CHAT_ITEMS_TABLE;

    public ChatItemsTable(Context context, int version) {
        super(context, DataBaseManager.FeedEntry.DB_NAME, null, version);
        this.context = context;
    }

    private static final String KEY_ID = "id";
    private static final String KEY_CONVERSATION_NAME = "conversation_name";
    private static final String KEY_SENDER_NAME = "sender_name";
    private static final String KEY_TEXT_MESSAGE = "text_message";
    private static final String KEY_IMAGE_PATH = "file_path";
    private static final String KEY_MESSAGE_DATE = "message_date";
    //***************************************************************************************************//

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + CHAT_ITEMS_TABLE);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CHAT_ITEMS_TABLE +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "conversation_name TEXT NOT NULL, " +
                "sender_name TEXT NOT NULL, " +
                "text_message TEXT, " +
                "file_path TEXT, " +
                "message_date TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_ITEMS_TABLE);

        // Create tables again
        onCreate(db);
    }
    //***************************************************************************************************//
    private void getTable(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + CHAT_ITEMS_TABLE);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CHAT_ITEMS_TABLE +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "conversation_name TEXT NOT NULL, " +
                "sender_name TEXT NOT NULL, " +
                "text_message TEXT, " +
                "file_path TEXT, " +
                "message_date TEXT NOT NULL)");

       // db.execSQL(CREATE_MESSAGES_TABLE);
    }
    //***************************************************************************************************//
    public long saveChatItemInTable(ChatItem chatItem, String addressedUserName) {
        SQLiteDatabase db = getWritableDatabase(); //TODO: do with ASYNC.
        getTable(db); //TODO: you should not get table here, it doesn't make sense
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_CONVERSATION_NAME, addressedUserName);
        }
        catch (NullPointerException e) {
            throw new ClassCastException(getClass().toString()
                    + "Addressed stylist passed from contactedUsersFrag is NULL!,  ");
        }
        values.put(KEY_SENDER_NAME, chatItem.getSenderName());
        values.put(KEY_IMAGE_PATH, chatItem.getImagePath());
        values.put(KEY_TEXT_MESSAGE, chatItem.getTextMessage());
        values.put(KEY_MESSAGE_DATE, chatItem.getMessageDate());
        long row_id = db.insert(CHAT_ITEMS_TABLE, null, values);
        db.close();
        long c = row_id;
        return c;
    }
    //***************************************************************************************************//
    public List<ChatItem> getAllSingleConversationChatItems(String conversationName[]) {
        //Conversation's name is the addressed user name

//Check if the addressedUser's array was already
// loaded into the AllConversationsArray
        List<ChatItem> convChatItems = AllConversationsHashMap.getInstance().getHashMap().get(conversationName[0]);

        if (convChatItems != null) {
            return convChatItems;
        }

        //Check if the user exists in the DB and, if not, return an empty list
        //In addition, save the chatItems to the AllConversationsArray
        //IMPORTANT: the method ALWAYS return list from A
        // ConversationsArray (empty or not)

        convChatItems = new ArrayList<>(); //it was null in AllConversationsArray
        String selectQuery = "SELECT sender_name," +
                "text_message,file_path,message_date FROM "
                + CHAT_ITEMS_TABLE + " WHERE " +
                KEY_CONVERSATION_NAME + "=?";

        SQLiteDatabase db = getReadableDatabase();
        getTable(db); //TODO: you should not get table here, it doesn't make sense
        Cursor cursor = db.rawQuery(selectQuery, conversationName);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String senderName = cursor.getString(0);
                String textMessage = cursor.getString(1);
                String filePath = cursor.getString(2);
                String messageDate = cursor.getString(3);

                ChatItem chatItem = createImageOrTextChatItem(senderName,
                        filePath, textMessage, messageDate);

                // Adding ChatItem to list
                convChatItems.add(chatItem);
            } while (cursor.moveToNext());
        }

        db.close();

        //put all chatItems from database (if any) to AllConversationArray
        AllConversationsHashMap.getInstance().getHashMap().put(conversationName[0], convChatItems);
        // return all ChatItems list
        return AllConversationsHashMap.getInstance().getHashMap().get(conversationName[0]);
    }
    //***************************************************************************************************//
    public void getAllConversations() {
        SQLiteDatabase db = getReadableDatabase();
        getTable(db);
        List<ChatItem> singleConversationChatItems;

        // Select All Query
        String selectAllConversationsNamesQuery = "SELECT conversation_name" +
                " FROM " + CHAT_ITEMS_TABLE;


        Cursor cursor = db.rawQuery(selectAllConversationsNamesQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // if needed, put conversation inside all conversations array
                String convName = cursor.getString(0);
                if (AllConversationsHashMap.getInstance().getHashMap().get(convName) == null) {
                    singleConversationChatItems = getAllSingleConversationChatItems(new String[]{convName});
                    AllConversationsHashMap.getInstance().getHashMap().put(convName, singleConversationChatItems);
                }
            } while (cursor.moveToNext());
        }
    }
//***************************************************************************************************//

    private ChatItem createImageOrTextChatItem(String senderName, String filePath, String textMessage, String mesageDate) {
        ChatItem chatItem;
        if (filePath == null) {
            chatItem = new ChatItem(context, senderName, textMessage, mesageDate);
        } else {
            chatItem = new ChatItem(context, senderName, "Photo from " + senderName , filePath, mesageDate);
        }
        return chatItem;
    }

}
