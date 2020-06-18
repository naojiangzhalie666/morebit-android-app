package com.zjzy.morebit.goodsvideo.presenter;


import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.model.VideoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;

import java.util.List;

import io.reactivex.functions.Action;

public class VideoPresenter extends MvpPresenter<VideoModel, VideoContract.View> implements VideoContract.Present{

    /*
    *
    * 获取抖货分类条目
    * */
    @Override
    public void getVideoClass(BaseActivity activity) {
        mModel.getVideoClass(activity)
                .compose(RxUtils.<BaseResponse<List<VideoClassBean>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<VideoClassBean>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<VideoClassBean>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onVideoClassError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(List<VideoClassBean> data) {
                        getIView().onVideoClassSuccess(data);

                    }

                });
    }

    @Override
    public void getVideoGoods(RxFragment rxFragment, String catId, int page) {
        mModel.getVideoGoods(rxFragment,catId,page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new DataObserver<List<ShopGoodInfo>>() {
            @Override
            protected void onDataListEmpty() {
                getIView().onVideoGoodsError();
            }

            @Override
            protected void onSuccess(List<ShopGoodInfo> data) {
                getIView().onVideoGoodsSuccess(data);
            }
        });
    }

    @Override
    public void getCommissionGoods(RxFragment rxFragment, String catId, int minId) {
        mModel.getCommissionGoods(rxFragment,catId,minId)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onCommissionGoodsError();
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        getIView().onCommissionGoodsSuccess(data);

                    }

                });
    }


}
