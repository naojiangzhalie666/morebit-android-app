package com.zjzy.morebit.goodsvideo.contract;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;

import java.util.List;

public class VideoContract {
    public interface View extends BaseView {


        void onVideoClassSuccess(List<VideoClassBean> shopGoodInfo);
        void onVideoClassError(String throwable);

        void onVideoGoodsSuccess(List<ShopGoodInfo> shopGoodInfo);
        void onVideoGoodsError();

        void onCommissionGoodsSuccess(List<ShopGoodInfo> shopGoodInfo);
        void onCommissionGoodsError();


    }
    public interface Present extends BasePresenter {


        /**
         * 抖货分类
         */
        void getVideoClass(BaseActivity activity);

        /**
         * 抖货分类商品
         */
        void getVideoGoods(RxFragment rxFragment, String catId, int page);
        /**
         * 高佣分类商品
         */
        void getCommissionGoods(RxFragment rxFragment, String catId, int minId);




    }
}
