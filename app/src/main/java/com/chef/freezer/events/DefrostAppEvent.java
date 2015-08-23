package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.Logger;
import com.chef.freezer.util.PackageHandling;

/**
 * Created by Brian on 8/8/2015.
 *
 * Event to defrost apps.
 */
public class DefrostAppEvent {

    private static final String TAG = "DefrostAppEvent";
    public AppCard daeae;
    private String command;

    public DefrostAppEvent(AppCard AE) {
        this.daeae = AE;
        this.command = PackageHandling.defrostapp(AE);
        Logger.logv(TAG, "daeae = " + daeae.toString());
        Logger.logv(TAG, "command = " + command);
    }

    public String getc() {
        return this.command;
    }

}
