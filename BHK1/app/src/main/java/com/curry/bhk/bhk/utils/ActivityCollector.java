package com.curry.bhk.bhk.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Curry on 2016/9/2.
 */
public class ActivityCollector {
    public static List<Activity> mActivities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
