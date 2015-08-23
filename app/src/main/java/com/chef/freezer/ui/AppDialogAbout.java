package com.chef.freezer.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Brian on 8/8/2015.
 * <p/>
 * Dialog about the application.
 */
public class AppDialogAbout extends DialogFragment {

    private static final String TAG = "AppDialogAbout";

    public static AppDialogAbout newInstance() {
        AppDialogAbout frag = new AppDialogAbout();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("About")
                .setMessage("Dfhjjjuffrdd")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel
                    }
                }).create();
    }

}
