package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.Logger;
import com.chef.freezer.util.PackageHandling;

/**
 * Created by Brian on 8/8/2015.
 *
 * Event to freeze apps.
 */
public class FreezeAppEvent {

    private static final String TAG = "FreezeAppEvent";
    public AppCard faeae;
    private String command;

    public FreezeAppEvent(AppCard AE) {
        this.faeae = AE;
        this.command = PackageHandling.freezeapp(AE);
        Logger.logv(TAG, "faeae = " + faeae.toString());
        Logger.logv(TAG, "command = " + command);
    }

    public String getc() {
        return this.command;
    }

}
