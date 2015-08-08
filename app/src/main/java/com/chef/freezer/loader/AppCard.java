package com.chef.freezer.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by Brian on 8/8/2015.
 */
public class AppCard {

    public String name;
    public String surname;
    public String email;

    public static final String NAME_PREFIX = "Name_";
    public static final String SURNAME_PREFIX = "Surname_";
    public static final String EMAIL_PREFIX = "email_";

    private final AppListLoader mLoader;
    public final ApplicationInfo mInfo;
    public final File mApkFile;
    public String mLabel;
    public Drawable mIcon;
    public boolean mMounted;

    public AppCard(AppListLoader loader, ApplicationInfo info) {
        mLoader = loader;
        mInfo = info;
        mApkFile = new File(info.sourceDir);
        mIcon = getIcon();
    }

    public StringBuilder maketext() {
        PackageInfo pi = getpackinfo(mInfo.packageName);
        StringBuilder sb = new StringBuilder();

        if (pi == null) {
            sb.append("Could not find package.");
            return sb;
        }

        sb.append("Version Name: ").append(nullcheck(pi.versionName));
        //sb.append((pi.versionName == null) ? "Version Name: None" : "Version Name: " + pi.versionName.toString());
        sb.append("\n");
        sb.append("Version Code: ").append(pi.versionCode);
        sb.append("\n");
        sb.append("APK: ").append(pi.applicationInfo.sourceDir);
        sb.append("\n");
        sb.append("Data: ").append(pi.applicationInfo.dataDir);
        sb.append("\n");
        sb.append("UID: ").append(pi.applicationInfo.uid);
        sb.append("\n");
        sb.append((pi.applicationInfo.processName == null) ? "Process: None" : "Process: " + pi.applicationInfo.processName);
        sb.append("\n");
        sb.append("Package: ").append(pi.applicationInfo.packageName);
        return sb;
    }

    public String nullcheck(String s) {
        return (s == null) ? "N/A" : s;
    }

    public String getprocessname(){
        String ProcessName = mInfo.processName;

        if(ProcessName != null){
            return ProcessName;
        } else{
            return "N/A";
        }
    }

    public String getversionname(){
        PackageInfo pi = getpackinfo(mInfo.packageName);
        String VersionName = "N/A";

        if (pi == null) {
            return VersionName;
        } else {
            return nullcheck(pi.versionName);
        }
    }

    public int getversioncode(){
        PackageInfo pi = getpackinfo(mInfo.packageName);
        int VersionCode = 0;

        if (pi == null) {
            return VersionCode;
        } else {
            return pi.versionCode;
        }
    }

    public PackageInfo getpackinfo(String packagename){
        PackageInfo pi = null;
        try {
            pi = mLoader.getContext().getPackageManager().getPackageInfo(packagename, 0);
            return pi;
        }
        catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public boolean isappfrozen() {
        return !getApplicationInfo().enabled;
    }

    public ApplicationInfo getApplicationInfo() {
        return mInfo;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getPackageName() {
        return mInfo.packageName;
    }

    public String getSourceDir() {
        return mInfo.sourceDir;
    }

    public String getDataDir() {
        return mInfo.dataDir;
    }

    public boolean ispackageuser() {
        return (mInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1;
    }

    public boolean isUpdatedSystemApp() {
        return (mInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            if (mApkFile.exists()) {
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            if (mApkFile.exists()) {
                mMounted = true;
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            }
        } else {
            return mIcon;
        }
        return mLoader.getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }

    @Override
    public String toString() {
        return mLabel;
    }

    void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = mInfo.packageName;
            }
            else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }

}
