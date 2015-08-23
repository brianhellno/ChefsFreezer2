package com.chef.freezer.util;

import android.util.Log;

/**
 * Created by Brian on 8/22/2015.
 * <p/>
 * Logging class.
 */
public class Logger {

    public static void logv(String TAG, String log) {
        if (Constants.LOGGING_ON == 1) {
            Log.v(TAG, log);
        }
    }

    public static void logd(String TAG, String log) {
        if (Constants.LOGGING_ON == 1) {
            Log.d(TAG, log);
        }
    }

    public static void logi(String TAG, String log) {
        if (Constants.LOGGING_ON == 1) {
            Log.i(TAG, log);
        }
    }

    public static void logw(String TAG, String log) {
        if (Constants.LOGGING_ON == 1) {
            Log.w(TAG, log);
        }
    }

    public static void loge(String TAG, String log) {
        if (Constants.LOGGING_ON == 1) {
            Log.e(TAG, log);
        }
    }

}
