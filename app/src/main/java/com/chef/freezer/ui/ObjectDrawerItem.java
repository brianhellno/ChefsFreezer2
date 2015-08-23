package com.chef.freezer.ui;

/**
 * Created by Brian on 8/22/2015.
 * <p/>
 * Used to hold the Drawer objects.
 */
public class ObjectDrawerItem {

    private static final String TAG = "ObjectDrawerItem";
    public int icon;
    public String name;

    public ObjectDrawerItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

}
