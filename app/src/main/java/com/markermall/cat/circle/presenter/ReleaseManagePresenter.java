package com.markermall.cat.circle.presenter;

import com.markermall.cat.circle.contract.ReleaseManageContract;
import com.markermall.cat.circle.model.ReleaseManageModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.ReleaseManage;
import com.markermall.cat.pojo.request.RequestReleaseGoodsDelete;
import com.markermall.cat.pojo.request.RequestReleaseManage;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ReleaseManagePresenter extends MvpPresenter<ReleaseManageModel, ReleaseManageContract.View> implements ReleaseManageContract.Present {




    @Override
    public void getReleaseManageList(RxFragment rxFragment, RequestReleaseManage body, CommonEmpty emptyView) {
        mModel.getReleaseManageList(rxFragment, body)
                .compose(emptyView.<BaseResponse<List<ReleaseManage>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onFinally();
                    }
                })
                .subscribe(new DataObserver<List<ReleaseManage>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onEmpty();
                    }


                    @Override
                    protected void onSuccess(List<ReleaseManage> data) {
                        getIView().onSuccessful(data);
                    }
                });
    }

    @Override
    public void getReleaseGoodsDelete(RxFragment rxFragment, RequestReleaseGoodsDelete body, final int position) {
        mModel.getReleaseGoodsDelete(rxFragment,body)
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        getIView().onNull(position);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        getIView().onGoodsDeleteSuc(data,position);
                    }
                });
    }
}
