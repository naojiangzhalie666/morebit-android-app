package com.zjzy.morebit.utils.helper;

import android.app.Activity;

import com.zjzy.morebit.utils.UI.WindowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by fengrs on 2018/8/6.
 * 备注: activity 启动 销户的管理
 */

public class ActivityLifeHelper {

    private static ActivityLifeHelper singleton;

    // Returns the application instance
    public static ActivityLifeHelper getInstance() {
        if (singleton == null) {
            singleton = new ActivityLifeHelper();
        }
        return singleton;
    }


    /**
     * 应用程序的默认栈栈顶对象类
     */
    private volatile Class<?> mStackTopClass;

    /**
     * 应用程序的 Activity 集合。
     */
    protected CopyOnWriteArrayList<Activity> mActivitySet = new CopyOnWriteArrayList<Activity>();


    /**
     * 获取应用程序的当前栈顶类。
     *
     * @return
     */
    public Class<?> getStackTopClass() {
        return mStackTopClass;
    }

    /**
     * 设置应用程序的当前栈顶类。
     *
     * @param stackTopClass ：栈顶类。
     */
    public void setStackTopClass(final Class<?> stackTopClass) {
        mStackTopClass = stackTopClass;
    }

    /**
     * 将指定的 {@link Activity} 对象添加到 Activity 集合中。
     *
     * @param activity ：指定的 {@link Activity} 对象。
     */
    public void addActivity(final Activity activity) {
        if (activity == null) {
            return;
        }

        mActivitySet.add(activity);
    }

    /**
     * 获取最上面的activity
     */
    public Activity getTopActivity() {
        return mActivitySet.get(mActivitySet.size() - 1);
    }

    /**
     * 移除并返回 Activity 集合中的 {@link Activity} 对象。
     *
     * @return {@code true} 表示移除成功，否则为 {@code false}。
     */
    public boolean removeActivity(final Activity activity) {
        if (activity == null) {
            return false;
        }

        return mActivitySet.remove(activity);
    }

    /**
     * 关闭所有 {@link Activity} 对象。
     */
    public final void finishAllActivities() {
        List<Activity> activityList = null;
        synchronized (mActivitySet) {
            activityList = new ArrayList<Activity>(mActivitySet);
        }
        for (Activity activity : activityList) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 关闭所有非指定的 {@link Activity} 对象。
     *
     * @param noColsedActivitys
     */
    public void finishAllActivities(Activity... noColsedActivitys) {
        List<Activity> activityList = null;
        synchronized (mActivitySet) {
            activityList = new ArrayList<Activity>(mActivitySet);
        }

        if (noColsedActivitys != null && noColsedActivitys.length > 0) {
            for (Activity activity : noColsedActivitys) {
                activityList.remove(activity);
            }
        }

        for (Activity activity : activityList) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 关闭所有非指定的 {@link Activity} 对象。
     *
     * @param noColsedActivitys
     */
    public void finishAllActivities(Class<?>... noColsedActivitys) {
        List<Activity> activityList = null;
        synchronized (mActivitySet) {
            activityList = new ArrayList<Activity>(mActivitySet);
        }

        if (noColsedActivitys != null && noColsedActivitys.length > 0) {
            for (Class activity : noColsedActivitys) {
                activityList.remove(activity);
            }
        }

        for (Activity activity : activityList) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 只留下当前的
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivitySet) {
            if (!activity.getClass().equals(cls)) {
                removeActivity(activity);
                activity.finish();
            }
        }
    }


    /**
     * 移除这一类
     */
    public void removeAllActivity(Class<?> cls) {
        for (Activity activity : mActivitySet) {
            if (activity.getClass().equals(cls)) {
                removeActivity(activity);
                activity.finish();
            }
        }
    }


    /**
     * 显示网络链接出错
     */
    public void addNotWorkLinkError() {
        for (Activity activity : mActivitySet) {
            WindowUtils.showPopupWindow(activity);
        }
    }

    /**
     * 隐藏网络链接出错
     */
    public void hideNotWorkLinkSucceed() {
        for (Activity activity : mActivitySet) {
            WindowUtils.hidePopupWindow(activity);
        }
    }

    /**
     * 返回集合中的实例(注意，如果一样的Activity嵌套多个Fragment，只取第一个activity)
     *
     * @param cls
     * @return
     */
    public Activity getActivityInstance(Class<?> cls) {
        Activity activity = null;
        for (Activity a : mActivitySet) {
            if (a.getClass().equals(cls)) {
                activity = a;
                break;
            }
        }
        return activity;
    }

    /**
     * 是否包含
     *
     * @param cls
     * @return
     */
    public boolean isContainsActivity(Class<?> cls) {
        for (Activity a : mActivitySet) {
            if (a.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }
}
