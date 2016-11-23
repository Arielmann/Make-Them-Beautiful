package com.example.home.makethembeautiful.contacted_users.events;

import com.example.home.makethembeautiful.contacted_users.model.ContactedUsersRow;

import java.util.List;

/**
 * Created by home on 11/4/2016.
 */
public class OnContactedUsersLoadedEvent {

    public List<ContactedUsersRow> contactedUsersRows;

    public OnContactedUsersLoadedEvent(List<ContactedUsersRow> contactedUsersRows) {
        this.contactedUsersRows = contactedUsersRows;
    }
}
