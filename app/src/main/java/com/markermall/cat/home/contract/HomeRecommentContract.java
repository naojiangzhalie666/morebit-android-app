package com.markermall.cat.home.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.FloorInfo;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.goods.HandpickBean;
import com.markermall.cat.pojo.goods.NewRecommendBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by fengrs
 * Data: 2018/9/7.
 */
public class HomeRecommentContract {
    public interface View extends BaseView {
        void onRecommendSuccessful(NewRecommendBean recommendBean);
        void onRecommendFailure(String errorMsg, String errCode);
        void onBrandBanner(List<ImageInfo> datas,int back);
        void onBannerFailure(String errorMsg,  int back);
        void onActivitySuccessFul(List<HandpickBean> data);
        void onActivityFailure( );
        void onFloorSuccess(List<FloorInfo> data);
        void onFloorFailure();
    }

    public interface Present extends BasePresenter {
        /**
         * 精品推荐
         */
        void getRecommend(RxFragment rxFragment,int page,String extra);
        /**
         * 新精品推荐
         */
        void getNewRecommend(RxFragment rxFragment,int page,int minNum,int type);
        /**
         * 广告轮播
         * @param fragment
         * @param back
         */
        void  getBanner(RxFragment fragment, int back);

        /**
         * 精选活动列表
         * @param fragment
         * @param page
         */
        void getActivities(RxFragment fragment, int page);

        /**
         * 楼层
         * @param fragment
         */
        void  getFloor(RxFragment fragment);



    }
}
