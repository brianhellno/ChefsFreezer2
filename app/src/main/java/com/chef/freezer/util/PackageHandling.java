package com.chef.freezer.util;

import android.content.pm.ApplicationInfo;

import com.chef.freezer.loader.AppCard;

/**
 * Created by Brian on 8/8/2015.
 * <p/>
 * Utility to create commands.
 */
public class PackageHandling {

    private static final String TAG = "PackageHandling";
    private static final String Uninstall = "pm uninstall ";
    private static final String Freeze = "pm disable ";
    private static final String Defrost = "pm enable ";

    public static String freezeapp(AppCard AE) {
        Logger.logv(TAG, Freeze + AE.getApplicationInfo().packageName);
        return Freeze + AE.getApplicationInfo().packageName;
    }

    public static String defrostapp(AppCard AE) {
        Logger.logv(TAG, Defrost + AE.getApplicationInfo().packageName);
        return Defrost + AE.getApplicationInfo().packageName;
    }

    public static String uninstallapp(AppCard AE) {
        StringBuilder sb = new StringBuilder();

        if ((AE.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
            sb.append(Uninstall + AE.getApplicationInfo().packageName);
        } else {
            sb.append("mount -o rw,remount /system").append("\n");
            sb.append("rm -R " + AE.getApplicationInfo().sourceDir.toString()).append("\n");
            sb.append("rm -R " + AE.getApplicationInfo().dataDir.toString()).append("\n");
            sb.append("mount -o ro,remount /system").append("\n");
            sb.append(Uninstall + AE.getApplicationInfo().packageName);
        }
        Logger.logv(TAG, "Uninstall Command: " + sb.toString());
        return sb.toString();
    }

}
