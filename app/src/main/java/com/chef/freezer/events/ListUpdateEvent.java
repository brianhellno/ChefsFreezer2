package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.ListHandler;
import com.chef.freezer.util.Logger;

import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 *
 * Event to update the listview.
 */
public class ListUpdateEvent {

    private static final String TAG = "ListUpdateEvent";
    List<AppCard> AEList;
    public int position;

    public ListUpdateEvent(List<AppCard> lae, int i) {
        this.AEList = lae;
        this.position = i;
        Logger.logv(TAG, "AEList = " + AEList.toString());
        Logger.logv(TAG, "position = " + i);
    }

    public List<AppCard> alllist() {
        return AEList;
    }

    public List<AppCard> userlist() {
        return ListHandler.mbuser(AEList);
    }

    public List<AppCard> systemlist() {
        return ListHandler.mbsystem(AEList);
    }

    public List<AppCard> frozenlist() {
        return ListHandler.mbdisabled(AEList);
    }

}
