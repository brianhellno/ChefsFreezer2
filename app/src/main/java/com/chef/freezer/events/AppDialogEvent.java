package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;

/**
 * Created by Brian on 8/22/2015.
 */
public class AppDialogEvent {

    public AppCard ac;

    public AppDialogEvent(AppCard a) {
        this.ac = a;
    }

}
