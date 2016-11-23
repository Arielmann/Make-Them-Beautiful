package com.example.home.makethembeautiful.db_manager;

import android.content.Context;
import android.provider.BaseColumns;

import com.example.home.makethembeautiful.chat.sqlite.ChatItemsTable;
import com.example.home.makethembeautiful.contacted_users.sqlite.ContactedUsersTableReader;
import com.example.home.makethembeautiful.contacted_users.sqlite.ContactedUsersTableWriter;

/**
 * Created by home on 8/30/2016.
 */
public class DataBaseManager {
    private static DataBaseManager dataBase;

    private ChatItemsTable chatItemsTable;
    private ContactedUsersTableReader contactedStylistsReader;
    private ContactedUsersTableWriter contactedStylistsWriter;
    private Context context;

    public static DataBaseManager getInstance(Context context) {
        if (dataBase == null) {
            dataBase = new DataBaseManager(context);
        }
        return dataBase;
    }

    private DataBaseManager(Context context) {
        chatItemsTable = new ChatItemsTable(context, 3);
        contactedStylistsReader = new ContactedUsersTableReader(context, 3);
        contactedStylistsWriter = new ContactedUsersTableWriter(context, 3);
    }

    public ChatItemsTable getChatItemsTable() {
        return chatItemsTable;
    }

    public ContactedUsersTableReader getContactedStylistsReader() {
        return contactedStylistsReader;
    }

    public ContactedUsersTableWriter getContactedUsersWriter() {
        return contactedStylistsWriter;
    }


    public static abstract class FeedEntry implements BaseColumns {
        public static final String DB_NAME = "mmbdata_db.sqlite";

        //FILES CONTRACT
        public static final String CONTACTED_USERS_TABLE = "contacted_users";
        public static final String CHAT_ITEMS_TABLE = "chat_items";
        public static final String FILE_ID = "entryid";
        public static final String FILE_PATH = "file_path";
        public static final String FILE_BYTES = "file_bytes";
        //public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}

