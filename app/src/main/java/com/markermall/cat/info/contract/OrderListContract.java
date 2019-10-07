package com.markermall.cat.info.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.ConsComGoodsInfo;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class OrderListContract {
    public interface View extends BaseView {
        void onSuccessful(List<ConsComGoodsInfo> datas);
        void onSearchSuccessful(ConsComGoodsInfo data);
        void onSearchSuccessful(List<ConsComGoodsInfo> data);
        void onFailure( );
        void onFinally();
        void onCheckGoodsSuccessFul(ShopGoodInfo data);
    }

    public interface Present extends BasePresenter {
        void getGoodsOrder(RxFragment rxFragment,int teamType,int order_type,int page);
        void searchGoodsOrder(RxFragment rxFragment, String orderSn, int type,int page);
        void onCheckGoods(RxFragment rxFragment, String itemSourceId);
    }
}
