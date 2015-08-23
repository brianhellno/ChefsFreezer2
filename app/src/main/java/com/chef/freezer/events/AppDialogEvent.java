package com.chef.freezer.events;

import com.chef.freezer.loader.AppCard;
import com.chef.freezer.util.Logger;

/**
 * Created by Brian on 8/22/2015.
 *
 * For showing dialogs about the app.
 */
public class AppDialogEvent {

    public AppCard ac;
    private static final String TAG = "AppDialogEvent";

    public AppDialogEvent(AppCard a) {
        this.ac = a;
        Logger.logv(TAG, "Assigned app to dialog: " + a.toString());
    }

}
