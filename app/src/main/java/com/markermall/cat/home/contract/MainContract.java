package com.markermall.cat.home.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.AppUpgradeInfo;
import com.markermall.cat.pojo.GrayUpgradeInfo;
import com.markermall.cat.pojo.ImageInfo;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * Created by fengrs
 * Data: 2019/4/15.
 */
public class MainContract {
    public interface View extends BaseView {
        /**
         *  显示升级
         * @param data
         */
        void showUpdateView(AppUpgradeInfo data);

        /**
         *  设置系统通知
         * @param data
         * @param isLoginSucceed
         */
        void setSysNotificationData(List<ImageInfo> data, boolean isLoginSucceed);

        void saveTaobaoLinkData(List<String> data);

        void showGrayUpgradeView(GrayUpgradeInfo upgradeInfo);
    }

    public interface Present extends BasePresenter {
        /**
         *  获取广告页数据
         * @param rxActivity
         */
        void getPicture(RxAppCompatActivity rxActivity);

        /**
         *  app升级
         * @param rxActivity
         */
        void getAppInfo(RxAppCompatActivity rxActivity);
        /**
         * 心跳接口
         */
        void putHeart(RxAppCompatActivity rxActivity);
        /**
         * 获取系统通知
         */
        void getSysNotification(RxAppCompatActivity rxActivity,boolean isLoginSucceed);

        /**
         * 获取淘宝跳转链接
         * @param rxAppCompatActivity
         */
        void getTaobaoLink(RxAppCompatActivity rxAppCompatActivity);

        /**
         *  灰度升级
         * @param rxActivity
         * @param data  原有版本更新的信息,用于比较创建时间
         */
        void getGrayUpgradeInfo(RxAppCompatActivity rxActivity,AppUpgradeInfo data);
    }
}
