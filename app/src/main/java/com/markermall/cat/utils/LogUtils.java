package com.markermall.cat.utils;

import android.util.Log;

import com.markermall.cat.BuildConfig;

/**
 * Log输出
 */

public class LogUtils {

    public static void Log(String name,String msg){
        try {
            if (BuildConfig.DEBUG) {
                Log.i(name, msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
  public static void e(String name,String msg){
        try {
            if (BuildConfig.DEBUG) {
                Log.e(name, msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 接口返回数据打印
     * @param msg
     */
    public static void LogRqJson(String msg){
        try {
            if (BuildConfig.DEBUG) {
                Log.i("rqJson---", msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
