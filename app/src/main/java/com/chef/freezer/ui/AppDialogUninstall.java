package com.chef.freezer.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.chef.freezer.events.UninstallAppEvent;
import com.chef.freezer.loader.AppCard;

import de.greenrobot.event.EventBus;

/**
 * Created by Brian on 8/8/2015.
 */
public class AppDialogUninstall extends DialogFragment {

    AppCard mAE;

    public static AppDialogUninstall newInstance(AppCard AE) {
        AppDialogUninstall frag = new AppDialogUninstall();
        frag.mAE = AE;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(mAE.getIcon())
                .setTitle(mAE.toString())
                .setMessage("Are you sure you want to remove this app? These changes cannot be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EventBus.getDefault().post(new UninstallAppEvent(mAE));
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Cancel Dialog
                            }
                        }).create();
    }

}
