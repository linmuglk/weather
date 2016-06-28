package com.moarea.app.maoweather.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import dalvik.annotation.TestTarget;

/**
 * Created by Johnny on 2016/6/28.
 */
public class ActivityCollector {

    private static List<Activity> sActivities = new ArrayList<>();


    public static void addActivity(Activity activity) {

        sActivities.add(activity);

    }

    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    public static void finishAll() {

        for (Activity activity : sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
