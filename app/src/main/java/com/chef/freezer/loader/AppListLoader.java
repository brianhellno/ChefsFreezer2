package com.chef.freezer.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import com.chef.freezer.observer.InstalledAppsObserver;
import com.chef.freezer.observer.SystemLocaleObserver;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppCard>> {

    final PackageManager mPm;
    //final int mListPosition;

    // We hold a reference to the Loaderï¿½s data here.
    private List<AppCard> mApps;


    public AppListLoader(Context ctx) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(ctx);
        mPm = getContext().getPackageManager();
        //mListPosition = position;
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    /**
     * This method is called on a background thread and generates a List of
     * {@link AppCard} objects. Each entry corresponds to a single installed
     * application on the device.
     */
    @Override
    public List<AppCard> loadInBackground() {

        List<ApplicationInfo> apps = mPm.getInstalledApplications(0);

        if (apps == null) {
            apps = new ArrayList<ApplicationInfo>();
        }

        // Create corresponding array of entries and load their labels.
        List<AppCard> entries = new ArrayList<AppCard>(apps.size());
        for (int i = 0; i < apps.size(); i++) {
            AppCard entry = new AppCard(this, apps.get(i));
            entry.loadLabel(getContext());

            entries.add(entry);

        }

        // Sort the list.
        Collections.sort(entries, ALPHA_COMPARATOR);

        return entries;
    }

    /*******************************************/
    /** (2) Deliver the results to the client **/
    /*******************************************/

    /**
     * Called when there is new data to deliver to the client. The superclass will
     * deliver it to the registered listener (i.e. the LoaderManager), which will
     * forward the results to the client through a call to onLoadFinished.
     */
    @Override
    public void deliverResult(List<AppCard> apps) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (apps != null) {
                releaseResources(apps);
                return;
            }
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<AppCard> oldApps = mApps;
        //List<AppEntry> oldApps = apps;
        mApps = apps;

        if (isStarted()) {
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(apps);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldApps != null && oldApps != apps) {
            releaseResources(oldApps);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loaderï¿½s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {

        if (mApps != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mApps);
        }

        // Register the observers that will notify the Loader when changes are made.
        if (mAppsObserver == null) {
            mAppsObserver = new InstalledAppsObserver(this);
        }

        if (mLocaleObserver == null) {
            mLocaleObserver = new SystemLocaleObserver(this);
        }

        if (takeContentChanged()) {
            // When the observer detects a new installed application, it will call
            // onContentChanged() on the Loader, which will cause the next call to
            // takeContentChanged() to return true. If this is ever the case (or if
            // the current data is null), we force a new load.
            forceLoad();
        } else if (mApps == null) {
            // If the current data is null... then we should make it non-null! :)
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {

        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'apps'.
        if (mApps != null) {
            releaseResources(mApps);
            mApps = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
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

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(apps);

        // The load has been canceled, so we should release the resources
        // associated with 'mApps'.
        releaseResources(apps);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    /**
     * Helper method to take care of releasing resources associated with an
     * actively loaded data set.
     */
    private void releaseResources(List<AppCard> apps) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    /*********************************************************************/
    /** (4) Observer which receives notifications when the data changes **/
    /*********************************************************************/

    // An observer to notify the Loader when new apps are installed/updated.
    private InstalledAppsObserver mAppsObserver;

    // The observer to notify the Loader when the system Locale has been changed.
    private SystemLocaleObserver mLocaleObserver;

    /**************************/
    /** (5) Everything else! **/
    /**************************/

    private static final Comparator<AppCard> ALPHA_COMPARATOR = new Comparator<AppCard>() {
        Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppCard object1, AppCard object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };

}
