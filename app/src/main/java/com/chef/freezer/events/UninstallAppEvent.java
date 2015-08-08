package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.PackageHandling;

/**
 * Created by Brian on 8/8/2015.
 */
public class UninstallAppEvent {

    public AppCard uaeae;
    public String command;

    public UninstallAppEvent(AppCard AE) {
        this.uaeae = AE;
        this.command = PackageHandling.uninstallapp(AE);
    }

    public String getc() {
        return this.command;
    }

}
