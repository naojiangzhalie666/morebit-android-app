package com.jf.my.goods.shopping.contract;

import com.jf.my.Activity.GoodsDetailActivity;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.ReleaseGoodsPermission;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.request.RequestReleaseGoods;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * Created by fengrs on 2018/8/17.
 */

public class GoodsDetailContract {
    public interface View extends BaseView {

        /**
         * 显示详情页数据
         * @param data
         * @param seavDao
         * @param isRefresh
         */
        void showDetailsView(ShopGoodInfo data, boolean seavDao, boolean isRefresh);

        void switchColler(ShopGoodInfo data);

        void OngetDetailDataFinally();
        void checkPermission(ReleaseGoodsPermission data);

        /**
         *  设置系统通知
         * @param data
         */
        void setSysNotificationData(List<ImageInfo> data);

        /**
         * 设置升级之后滴view
         * @param sysValue
         */
        void setUpdateView(String sysValue);

        /**
         * 设置详情页图片请求地址
         * @param data
         * @param moduleDescUrl
         */
        void setModuleDescUrl(ShopGoodInfo data, String moduleDescUrl);

        /**
         * 预加载淘口令
         */
        void getTaoKouLing();
    }

    public interface Present extends BasePresenter {

        /**
         *  获取详情页信息
         * @param rxActivity
         * @param goodsInfo
         */

        void getDetailData(BaseActivity rxActivity, ShopGoodInfo goodsInfo,boolean isRefresh);

        /**
         * 切换收藏
         * @param goodsInfo
         */
        void switchCollect(BaseActivity rxActivity,ShopGoodInfo goodsInfo);

        /**
         * 跳转店铺详情
         * @param goodsDetailActivity
         * @param goodsInfo
         */
        void getShopList(BaseActivity goodsDetailActivity, ShopGoodInfo goodsInfo);

        void saveGoodsHistor(GoodsDetailActivity activity, ShopGoodInfo itemSourceId);
        /**
         * 获取系统通知
         */
        void getSysNotification(RxAppCompatActivity rxActivity);
        /**
         *  获取普通会员，VIP，运营商佣金比例
         */
        void getSysCommissionPercent(RxAppCompatActivity rxActivity);
        void checkPermission(RxAppCompatActivity rxActivity, RequestReleaseGoods releaseGoods);
    }
}
