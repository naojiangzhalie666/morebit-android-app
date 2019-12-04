package com.zjzy.morebit.circle.presenter;

import com.zjzy.morebit.circle.contract.ReleaseManageContract;
import com.zjzy.morebit.circle.model.ReleaseManageModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.request.RequestReleaseGoodsDelete;
import com.zjzy.morebit.pojo.request.RequestReleaseManage;
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
