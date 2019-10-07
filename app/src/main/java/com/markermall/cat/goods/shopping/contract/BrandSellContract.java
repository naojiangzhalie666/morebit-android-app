package com.markermall.cat.goods.shopping.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.BrandSell;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/8/17.
 */

public class BrandSellContract {
    public interface View extends BaseView {

        void showSuccessful(List<BrandSell> brandSells);

        void showError();

        void getGoodsSuccessful(List<BrandSell> brandSells);

        void showFinally();
    }

    public interface Present extends BasePresenter {
        void getBrandSellList(RxFragment fragment, int page);

        void getBrandSellGoodsList(RxFragment fragment, int page);
    }
}
