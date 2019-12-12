package com.zjzy.morebit.goods.shopping.contract;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.BrandSell;
import com.zjzy.morebit.pojo.number.NumberGoods;

import java.util.List;

/**
 * 会员商品
 * Created by haiping.liu on 2019-12-11.
 */
public class NumberGoodsListContract {

    public interface View extends BaseView {

        void showSuccessful(List<NumberGoods> brandSells);

        void showError();


        void showFinally();
    }

    public interface Present extends BasePresenter {

        void getNumberGoodsList(RxFragment fragment, int page);
    }
}
