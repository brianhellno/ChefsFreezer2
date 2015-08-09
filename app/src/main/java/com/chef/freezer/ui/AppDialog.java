package com.chef.freezer.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.chef.freezer.events.DefrostAppEvent;
import com.chef.freezer.events.FreezeAppEvent;
import com.chef.freezer.events.UninstallAppCheckEvent;
import com.chef.freezer.loader.AppCard;

import de.greenrobot.event.EventBus;

/**
 * Created by Brian on 8/8/2015.
 */
public class AppDialog extends DialogFragment {

    AppCard mAE;

    public static AppDialog newInstance(AppCard AE) {
        AppDialog frag = new AppDialog();
        frag.mAE = AE;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String enabled = "Freeze";
        String launch = "Launch App";

        if (mAE.isappfrozen()) enabled = "Defrost";

        final Intent LaunchIntent = AppDialog.this.getActivity().getApplicationContext()
                .getPackageManager().getLaunchIntentForPackage(mAE.getApplicationInfo().packageName);

        if (LaunchIntent == null) launch = "Cancel";

        return new AlertDialog.Builder(getActivity())
                .setIcon(mAE.mIcon)
                .setTitle(mAE.toString())
                .setMessage(mAE.maketext())
                .setPositiveButton(launch, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (LaunchIntent != null) {
                            startActivity(LaunchIntent);
                        } else {
                        }
                    }
                })
                .setNegativeButton(enabled, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mAE.getApplicationInfo().enabled) {
                            EventBus.getDefault().post(new FreezeAppEvent(mAE));
                        }
                        else {
                            EventBus.getDefault().post(new DefrostAppEvent(mAE));
                        }
                    }
                })
                .setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EventBus.getDefault().post(new UninstallAppCheckEvent(mAE));
                    }
                }).create();
    }

}
