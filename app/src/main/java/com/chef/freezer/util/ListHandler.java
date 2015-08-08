package com.chef.freezer.util;

import android.content.pm.ApplicationInfo;

import com.chef.freezer.loader.AppCard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Brian on 8/8/2015.
 */
public class ListHandler {

    public interface Checker<T> {
        boolean check(T obj);
    }

    public static class SystemAppChecker implements Checker<AppCard> {
        public boolean check(AppCard AE) {
            return ((AE.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) == 1);
        }
    }

    public static class UserAppChecker implements Checker<AppCard> {
        public boolean check(AppCard AE) {
            return ((AE.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) != 1);
        }
    }

    public static class DisabledAppChecker implements Checker<AppCard> {
        public boolean check(AppCard AE) {
            return !AE.getApplicationInfo().enabled;
        }
    }

    public static <T> Collection<T> findAll(Collection<T> coll, Checker<T> chk) {
        LinkedList<T> l = new LinkedList<T>();
        for (T obj : coll) {
            if (chk.check(obj))
                l.add(obj);
        }
        return l;
    }

    public static List<AppCard> mbuser(List<AppCard> LAE) {
        Collection<AppCard> mUser = ListHandler.findAll(LAE, new UserAppChecker());
        return (List<AppCard>) mUser;
    }

    public static List<AppCard> mbsystem(List<AppCard> LAE) {
        Collection<AppCard> mSystem = ListHandler.findAll(LAE, new SystemAppChecker());
        return (List<AppCard>) mSystem;
    }

    public static List<AppCard> mbdisabled(List<AppCard> LAE) {
        Collection<AppCard> mDisabled = ListHandler.findAll(LAE, new DisabledAppChecker());
        return (List<AppCard>) mDisabled;
    }

}
