package com.example.home.makethembeautiful.contactedusers.model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.home.makethembeautiful.contactedusers.events.OnContactedUsersLoadedEvent;
import com.example.home.makethembeautiful.contactedusers.adapter.ContactedUsersAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactedUsersModel {


    /*
    * This singleton model manages the loading of
    * contacted users and response to runtime changes
    *
    * it is INDIRECTLY responsible to set the
    * OnContactedUserClickedMethod, by setting the ContactedUsersAdapter
    * the will directly set it.
    */

    private static ContactedUsersAdapter adapter;
    private static LinearLayoutManager layoutManager;
    private static ContactedUsersModel contactedUsersModel;
    private static List<ContactedUsersRow> dataSet = new ArrayList();
    private static Context context;
    private static String TAG = ContactedUsersModel.class.getSimpleName();

    public static ContactedUsersModel getInstance(Context insideMethodContext) {
        if (contactedUsersModel == null) {
            context = insideMethodContext;
            contactedUsersModel = new ContactedUsersModel(context);
        }
        layoutManager = (new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        return contactedUsersModel;
    }

    private ContactedUsersModel(Context context) {
        layoutManager = (new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public ContactedUsersAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter() {
        if (adapter == null) {
            initDataSet();
            adapter = new ContactedUsersAdapter(context, dataSet);
        }
    }

    public static List<ContactedUsersRow> initDataSet() { //Since singleton hashMap is use to determine dataSet, each change in the hashMap has to provoke this method
        dataSet.clear();
        HashMap<String, ContactedUsersRow> hashMap = ContactedUsersRowsHashMap.getInstance().getHashMap();
        if (!hashMap.isEmpty()) {
            for (Map.Entry<String, ContactedUsersRow> map : hashMap.entrySet()) {
                dataSet.add(map.getValue());
            }
        }
        EventBus.getDefault().post(new OnContactedUsersLoadedEvent(dataSet));
        return dataSet;
    }

    public List getDataSet() {
        return dataSet;
    }
}