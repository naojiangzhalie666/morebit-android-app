package com.zjzy.morebit.main.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.main.contract.CollectContract;
import com.zjzy.morebit.main.model.MainModel;
import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class CollectPresenter extends MvpPresenter<MainModel, CollectContract.View> implements CollectContract.Present {

    @Override
    public void getCollectData(CollectFragment2 fragment, int page) {

        mModel.getCollectData(fragment, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onDataListEmpty() {
                        getIView().showEmity();

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().showCollectFailure(errorMsg,errCode);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        getIView().showCollectSuccess(data);

                    }

                });
    }


    @Override
    public void getDeleteCollection(RxFragment fragment, String ids) {
        mModel.delUserCollection((BaseActivity) fragment.getActivity(), ids)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        getIView().showDeleteSuccess();
                    }
                });
    }


}
