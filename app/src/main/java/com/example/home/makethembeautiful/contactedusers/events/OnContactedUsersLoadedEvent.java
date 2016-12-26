package com.example.home.makethembeautiful.contactedusers.events;

import com.example.home.makethembeautiful.contactedusers.model.ContactedUsersRow;

import java.util.List;

public class OnContactedUsersLoadedEvent {

    public List<ContactedUsersRow> contactedUsersRows;

    public OnContactedUsersLoadedEvent(List<ContactedUsersRow> contactedUsersRows) {
        this.contactedUsersRows = contactedUsersRows;
    }
}
