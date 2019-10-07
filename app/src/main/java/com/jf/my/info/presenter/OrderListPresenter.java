package com.jf.my.info.presenter;

import com.jf.my.Module.push.Logger;
import com.jf.my.info.contract.OrderListContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ConsComGoodsInfo;
import com.jf.my.pojo.ShopGoodInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class OrderListPresenter extends MvpPresenter<InfoModel, OrderListContract.View> implements OrderListContract.Present {
    private static int taobaoOrder = 1;//：1表示淘宝订单
    private static int MYOrder = 2;//2表示蜜源生活订单

    @Override
    public void getGoodsOrder(RxFragment rxFragment, int teamType, int order_type, int page) {


            mModel.getGoodsOrder(rxFragment, order_type, teamType,page)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getIView().onFinally();
                        }
                    }).subscribe(new DataObserver<List<ConsComGoodsInfo>>() {
                @Override
                protected void onDataListEmpty() {
                    getIView().onFailure();
                }

                @Override
                protected void onSuccess(List<ConsComGoodsInfo> data) {
                    getIView().onSuccessful(data);
                }
            });


    }

    private int mPage = 1;

    @Override
    public void searchGoodsOrder(RxFragment rxFragment, String orderSn, int type, int page) {
        mPage = page;
        mModel.searchGoodsOrder(rxFragment, orderSn, type, mPage)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onFinally();
                        Logger.e("==showFinally==");
                    }
                })
                .subscribe(new DataObserver<List<ConsComGoodsInfo>>() {

                    @Override
                    protected void onDataListEmpty() {
                        getIView().onFailure();
                        Logger.e("==onDataListEmpty==");
                    }

                    @Override
                    protected void onSuccess(List<ConsComGoodsInfo> data) {
                        mPage++;
                        getIView().onSearchSuccessful(data);
                        Logger.e("==onSuccess==");
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                          getIView().onFailure();
                        Logger.e("==onError==" + errorMsg);
                    }
                });
    }

    @Override
    public void onCheckGoods(RxFragment rxFragment, String itemSourceId) {
        mModel.onCheckGoods(rxFragment, itemSourceId)
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(ShopGoodInfo data) {
                         getIView().onCheckGoodsSuccessFul(data);
                    }
                });
    }
}
