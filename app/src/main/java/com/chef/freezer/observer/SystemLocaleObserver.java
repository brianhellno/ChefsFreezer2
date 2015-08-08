package com.chef.freezer.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.chef.freezer.loader.AppListLoader;

/**
 * Created by Brian on 8/8/2015.
 */
public class SystemLocaleObserver extends BroadcastReceiver {

    private AppListLoader mLoader;

    public SystemLocaleObserver(AppListLoader loader) {
        mLoader = loader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        mLoader.getContext().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Tell the loader about the change.
        mLoader.onContentChanged();
    }

}
