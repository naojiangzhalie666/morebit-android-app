package com.markermall.cat.utils.fire;

import android.content.Context;

import com.markermall.cat.App;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.utils.C;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by fengrs on 2018/7/7.
 * 自定义错误
 */

public class BuglyUtils {

    /**
     * 上传自定义错误
     *
     * @param text
     */
    public static void putErrorToBugly(String text) {


        try {
            if (text == null) {
                return;
            }
            CrashReport.postCatchedException(new RegisterErrorBugly(C.Setting.appName + UserLocalData.mPhone + text));  // bugly会将这个throwable上报
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void e(String tag, String msg) {

        BuglyLog.e(C.Setting.appName + tag, msg);
    }

    public static void putData(String tag, String msg) {

        CrashReport.putUserData(App.getAppContext(), tag, msg);
    }

    /**
     * 上传自定义错误
     *
     * @param throwable
     */
    public static void putErrorToBugly(Throwable throwable) {


//        BuglyLog.e("goTaobaoTag", "start ");
//        BuglyLog.e("goTaobaoTag", "start 1");
//        BuglyLog.e("goTaobaoTag", "start 2");
//        BuglyLog.e("goTaobaoTag", "start 3");
//        CrashReport.postCatchedException(new MyException("qqqqqqqqqqqq ！"));  // bugly会将这个throwable上报
//        CrashReport.postCatchedException(new MyException("12333333 ！"));  // bugly会将这个throwable上报
//        CrashReport.postCatchedException(new SocketTimeoutException());  // bugly会将这个throwable上报
        CrashReport.postCatchedException(throwable);  // bugly会将这个throwable上报
    }

    // 上报后的Crash会显示该标签
    public static void setUserSceneTag(Context context, int goTaobaoTag) {

        CrashReport.setUserSceneTag(context, goTaobaoTag); // 上报后的Crash会显示该标签

    }
}
