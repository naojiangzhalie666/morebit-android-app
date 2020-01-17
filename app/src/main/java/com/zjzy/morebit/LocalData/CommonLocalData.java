package com.zjzy.morebit.LocalData;

import android.os.SystemClock;

import com.zjzy.morebit.App;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.SharedPreferencesUtils;

/**
 * 公共内容使用
 * Created by haiping.liu on 2019-12-26.
 */
public class CommonLocalData {

    /**
     * 获取同步后的服务端时间
     * @return
     */
    public static long syncServiceTime( ){
        long currentTime= SystemClock.elapsedRealtime();
        Long clientTime = (Long)SharedPreferencesUtils.get(App.getAppContext(), C.syncTime.CLIENT_TIME,0L);
        Long serverTime = (Long)SharedPreferencesUtils.get(App.getAppContext(), C.syncTime.SERVER_TIME,0L);
        return currentTime - clientTime + serverTime;

    }

    public static void saveClientTime(){
        SharedPreferencesUtils.put(App.getAppContext(), C.syncTime.CLIENT_TIME, SystemClock.elapsedRealtime());
    }

    /**
     * 保存服务端的时间
     * @param serverTime
     */
    public static void saveServiceTime(Long serverTime){
        SharedPreferencesUtils.put(App.getAppContext(), C.syncTime.SERVER_TIME, serverTime);
    }

    public static boolean getAuthedStatus(){
      return   (boolean)SharedPreferencesUtils.get(App.getAppContext(), C.authPrivate.IS_AUTHED, false);
    }

    //私有权限授权
    public static void authedPrivate(){
        SharedPreferencesUtils.put(App.getAppContext(), C.authPrivate.IS_AUTHED, true);
    }

}
