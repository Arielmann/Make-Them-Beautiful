package com.example.home.makethembeautiful.chat.model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.home.makethembeautiful.chat.adapter.ChatAdapter;
import com.example.home.makethembeautiful.chat.sqlite.ChatItemsTable;
import com.example.home.makethembeautiful.contactedusers.sqlite.ContactedUsersTableWriter;
import com.example.home.makethembeautiful.dbmanager.DataBaseManager;
import com.example.home.makethembeautiful.profile.profilemodels.User;

import java.util.List;

public class ChatDataModel {

    private Context context;
    private ChatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private User addressedUser;
    private List<ChatItem> chatItems;

    public ChatDataModel(Context context, User addressedUser) {
        this.context = context;
        this.addressedUser = addressedUser;
        this.layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        setChatItems();
        adapter = new ChatAdapter(context, chatItems);
    }

    //********************Getters*********************//
    public Context getContext() {
        return context;
    }

    public ChatItemsTable getChatItemsTable() {
        return DataBaseManager.getInstance(context).getChatItemsTable();
    }

    public ContactedUsersTableWriter getUsersTableWriter() {
        return DataBaseManager.getInstance(context).getContactedUsersWriter();
    }

    public ChatAdapter getAdapter() {

        return adapter; //TODO: Find out why does it being called twice??
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public User getAddressedUser() {
        return addressedUser;
    }

    public List<ChatItem> getChatItems() {
        return chatItems;
    }

    //*****************Setters**************//

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAdapter(ChatAdapter adapter) {
        this.adapter = adapter;
    }

    public void setChatItems() {
        try {
            List<ChatItem> addressedUserChatItems = AllConversationsHashMap.getInstance()
                    .getHashMap().get(addressedUser.getName());
            if (addressedUserChatItems == null) {
                //if it's not in DB, set chatItems to be empty array
                chatItems = getChatItemsTable().getAllSingleConversationChatItems(new String[]{addressedUser.getName()});
                AllConversationsHashMap.getInstance()
                        .getHashMap().put(addressedUser.getName(), chatItems);
                return;
            }
            chatItems = addressedUserChatItems;
        } catch (NullPointerException e) {
            Log.e("Chat Data Model", "null pointer exception, addressed user might be null");
        }
    }
}

