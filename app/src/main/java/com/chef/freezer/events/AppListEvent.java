package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;

import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 */
public class AppListEvent {

    public List<AppCard> AEList;

    public AppListEvent(List<AppCard> lae) {
        this.AEList = lae;
    }

}
