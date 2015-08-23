package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.Logger;
import com.chef.freezer.util.PackageHandling;

/**
 * Created by Brian on 8/8/2015.
 */
public class UninstallAppEvent {

    private static final String TAG = "UninstallAppEvent";
    public AppCard uaeae;
    public String command;

    public UninstallAppEvent(AppCard AE) {
        this.uaeae = AE;
        this.command = PackageHandling.uninstallapp(AE);
        Logger.logv(TAG, "uaeae = " + uaeae.toString());
        Logger.logv(TAG, "command = " + command);
    }

    public String getc() {
        return this.command;
    }

}
