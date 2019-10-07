package com.jf.my.info.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.ConsComGoodsInfo;
import com.jf.my.pojo.ShopGoodInfo;
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
