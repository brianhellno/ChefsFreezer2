package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.PackageHandling;

/**
 * Created by Brian on 8/8/2015.
 */
public class DefrostAppEvent {

    public AppCard daeae;
    public String command;

    public DefrostAppEvent(AppCard AE) {
        this.daeae = AE;
        this.command = PackageHandling.defrostapp(AE);
    }

    public String getc() {
        return this.command;
    }

}
