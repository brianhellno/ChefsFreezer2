package com.chef.freezer.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import com.chef.freezer.observer.InstalledAppsObserver;
import com.chef.freezer.observer.SystemLocaleObserver;
import com.chef.freezer.util.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 *
 * Loader to get the installed applications.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppCard>> {

    private static final String TAG = "AppListLoader";
    final PackageManager mPm;
    
    private List<AppCard> mApps;


    public AppListLoader(Context ctx) {
        super(ctx);
        mPm = getContext().getPackageManager();
    }

    @Override
    public List<AppCard> loadInBackground() {

        List<ApplicationInfo> apps = mPm.getInstalledApplications(0);

        if (apps == null) {
            apps = new ArrayList<ApplicationInfo>();
        }

        List<AppCard> entries = new ArrayList<AppCard>(apps.size());
        for (int i = 0; i < apps.size(); i++) {
            AppCard entry = new AppCard(this, apps.get(i));
            entry.loadLabel(getContext());
            Logger.logv(TAG, entry.getLabel());
            entries.add(entry);
        }

        Collections.sort(entries, ALPHA_COMPARATOR);
        return entries;
    }

    @Override
    public void deliverResult(List<AppCard> apps) {
        if (isReset()) {
            if (apps != null) {
                releaseResources(apps);
                return;
            }
        }

        List<AppCard> oldApps = mApps;
        //List<AppEntry> oldApps = apps;
        mApps = apps;

        if (isStarted()) {
            super.deliverResult(apps);
        }

        if (oldApps != null && oldApps != apps) {
            releaseResources(oldApps);
        }
    }

	private InstalledAppsObserver mAppsObserver;
    private SystemLocaleObserver mLocaleObserver;
	
	
    @Override
    protected void onStartLoading() {

        if (mApps != null) {
            deliverResult(mApps);
        }

        if (mAppsObserver == null) {
            mAppsObserver = new InstalledAppsObserver(this);
        }

        if (mLocaleObserver == null) {
            mLocaleObserver = new SystemLocaleObserver(this);
        }

        if (takeContentChanged()) {
            forceLoad();
        } else if (mApps == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
		
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mApps != null) {
            releaseResources(mApps);
            mApps = null;
        }

        if (mAppsObserver != null) {
            getContext().unregisterReceiver(mAppsObserver);
            mAppsObserver = null;
        }

        if (mLocaleObserver != null) {
            getContext().unregisterReceiver(mLocaleObserver);
            mLocaleObserver = null;
        }
    }

    @Override
    public void onCanceled(List<AppCard> apps) {
        super.onCanceled(apps);
        releaseResources(apps);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    private void releaseResources(List<AppCard> apps) {
    }

    private static final Comparator<AppCard> ALPHA_COMPARATOR = new Comparator<AppCard>() {
        final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(AppCard object1, AppCard object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };

}
