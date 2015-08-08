package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.PackageHandling;

/**
 * Created by Brian on 8/8/2015.
 */
public class FreezeAppEvent {

    public AppCard faeae;
    public String command;

    public FreezeAppEvent(AppCard AE) {
        this.faeae = AE;
        this.command = PackageHandling.freezeapp(AE);
    }

    public String getc() {
        return this.command;
    }

}
