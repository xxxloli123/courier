package com.android.slowlifecourier.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class CacheActivity {
    public static List<Activity> activityList = new LinkedList<Activity>();

    public CacheActivity() {

    }

    /**
     * 添加到Activity容器中
     */
    public static void addActivity(Activity activity) {
    }

    /**
     * 遍历所有Activigty并finish
     */
    public static void finishActivity() {
    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
    }
}
