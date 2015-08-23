package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.Logger;

/**
 * Created by Brian on 8/8/2015.
 *
 * Event to show dialog for uninstalling apps.
 */
public class UninstallAppCheckEvent {

    private static final String TAG = "UninstallAppCheckEvent";
    public AppCard uaceae;

    public UninstallAppCheckEvent(AppCard AE) {
        this.uaceae = AE;
        Logger.logv(TAG, "uaceae = " + uaceae.toString());
    }

}
