package com.chef.freezer.util;

import com.chef.freezer.events.DialogCancelEvent;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Brian on 8/8/2015.
 */
public class RootUtil {

    public static void rootcommandtest(String s) {

        Command command = new Command(0, s) {
            @Override
            public void commandOutput(int id, String line) {
                super.commandOutput(id, line);
                RootTools.log("TEST", line);
            }

            @Override
            public void commandTerminated(int id, String reason) {
                super.commandTerminated(id, reason);
                RootTools.log("TEST", reason);
                EventBus.getDefault().post(new DialogCancelEvent());
            }

            @Override
            public void commandCompleted(int id, int exitcode) {
                super.commandCompleted(id, exitcode);
                RootTools.log("TEST", "Exit Code: " + exitcode);
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
