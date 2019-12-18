package com.zjzy.morebit.goods.shopping.contract;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;

import java.util.List;

/**
 * 会员商品联系
 * Created by fengrs on 2018/8/17.
 */

public class NumberGoodsDetailContract {

    public interface View extends BaseView {

        void showSuccessful(NumberGoodsInfo goodsInfo);

        void onError();

    }

    public interface Present extends BasePresenter {

        void getGoodsDetail(BaseActivity rxActivity,String goodsId);
    }
}
