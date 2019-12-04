package com.zjzy.morebit.utils.appDownload;

import android.app.Activity;

import com.zjzy.morebit.pojo.AppUpgradeInfo;
import com.zjzy.morebit.utils.appDownload.update_app.UpdateAppBean;
import com.zjzy.morebit.utils.appDownload.update_app.UpdateAppManager;
import com.zjzy.morebit.utils.appDownload.update_app.listener.ExceptionHandler;

/**
 * Created by fengrs on 2018/7/12.
 */

public class QianWenUpdateUtlis {


    /**
     * 最简方式
     */
    public static void updateApp(Activity activity, String mUpdateUrl, UpdateAppBean response) {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(activity)
                //更新地址
                .setUpdateUrl(mUpdateUrl)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil(response))
                .build()
                .update();
    }

    /**
     * 强制更新
     */
    public static void constraintUpdate(Activity activity, String mUpdateUrl, UpdateAppBean response) {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(activity)
                //更新地址
                .setUpdateUrl(mUpdateUrl)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil(response))
                .build()
                .update();
    }

    /**
     * 静默下载，下载完才弹出升级界面
     */
    public static void onlyUpdateApp(Activity activity, String mUpdateUrl, UpdateAppBean response) {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(activity)
                //更新地址
                .setUpdateUrl(mUpdateUrl)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil(response))
                //只有wifi下进行，静默下载(只对静默下载有效)
                .setOnlyWifi()
                .build()
                .silenceUpdate();
    }

    public static UpdateAppBean getData(AppUpgradeInfo data ) {
        UpdateAppBean appUpdateBean = new UpdateAppBean();

        appUpdateBean.setNewVersion(data.getEdition());
        appUpdateBean.setApkFileUrl(data.getDownload());

        appUpdateBean.setUpdateLog(data.getInfo());
        appUpdateBean.setTargetSize(data.getSize());
        appUpdateBean.setNewMd5(data.getMd5());
        appUpdateBean.setUpdate("Yes");
        return appUpdateBean;
    }
}
