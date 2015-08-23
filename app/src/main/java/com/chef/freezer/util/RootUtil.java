package com.chef.freezer.util;

import com.chef.freezer.events.DialogCancelEvent;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import de.greenrobot.event.EventBus;

/**
 * Created by Brian on 8/8/2015.
 * <p/>
 * Utility for root.
 */
public class RootUtil {

    private static final String TAG = "RootUtil";

    public static void rootcommand(String s) {
        Command command = new Command(0, s) {
            @Override
            public void commandOutput(int id, String line) {
                super.commandOutput(id, line);
                Logger.logv(TAG, line);
            }

            @Override
            public void commandTerminated(int id, String reason) {
                super.commandTerminated(id, reason);
                Logger.logv(TAG, reason);
                EventBus.getDefault().post(new DialogCancelEvent());
            }

            @Override
            public void commandCompleted(int id, int exitcode) {
                super.commandCompleted(id, exitcode);
                Logger.logv(TAG, "Exit Code: " + exitcode);
                EventBus.getDefault().post(new DialogCancelEvent());
            }
        };
        try {
            RootTools.getShell(true).add(command);
        } catch (IOException | RootDeniedException | TimeoutException ex) {
            ex.printStackTrace();
        }
    }

}