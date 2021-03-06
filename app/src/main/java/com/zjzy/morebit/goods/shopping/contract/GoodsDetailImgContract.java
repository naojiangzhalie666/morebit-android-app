package com.zjzy.morebit.goods.shopping.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by fengrs on 2018/8/17.
 */

public class GoodsDetailImgContract {
    public interface View extends BaseView {

        void showFinally();

        /**
         * 获取web成功
         */
        void showImgWebSuccess(String data);


        /**
         * 获取img成功
         */
        void showDeImgSuccess( List<String> arrayList);
    }

    public interface Present extends BasePresenter {


        void getModuleDescUrlData(RxFragment fragment, ShopGoodInfo goodsInfo, GoodsImgDetailBean picUrls, int analysisFlag);
    }
}
