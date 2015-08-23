package com.chef.freezer.events;

import com.chef.freezer.util.Logger;

/**
 * Created by Brian on 8/8/2015.
 *
 * Make the call for the list event.
 */
public class AppListEvent {

    private static final String TAG = "AppListEvent";

    public AppListEvent(){
        Logger.logv(TAG, "Event for app list.");
    }
}
