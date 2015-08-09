package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.ListHandler;

import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 */
public class ListUpdateEvent {

    List<AppCard> AEList;
	public int position;

    public ListUpdateEvent(List<AppCard> lae, int i) {
        this.AEList = lae;
		this.position = i;
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
