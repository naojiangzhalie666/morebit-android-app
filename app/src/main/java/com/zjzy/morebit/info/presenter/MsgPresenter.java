package com.zjzy.morebit.info.presenter;

import com.zjzy.morebit.info.contract.MsgContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class MsgPresenter extends MvpPresenter<InfoModel, MsgContract.View> implements MsgContract.Present {

    public void getMsg(RxFragment fragment,int type, int page, CommonEmpty emptyView) {
        mModel.getMsg(fragment,type, page)
                .compose(emptyView.<BaseResponse<List<EarningsMsg>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onMsgFinally();
                    }
                })
                .subscribe(new DataObserver<List<EarningsMsg>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onMsgfailure();
                    }



                    @Override
                    protected void onSuccess(List<EarningsMsg> data) {
                        getIView().onMsgSuccessful(data);
                    }
                });
    }

    public void getFeedbackMsg(RxFragment fragment, int page, CommonEmpty emptyView) {
        mModel.getFeedbackMsg(fragment, page)
                .compose(emptyView.<BaseResponse<List<EarningsMsg>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onMsgFinally();
                    }
                })
                .subscribe(new DataObserver<List<EarningsMsg>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onMsgfailure();
                    }



                    @Override
                    protected void onSuccess(List<EarningsMsg> data) {
                        getIView().onMsgSuccessful(data);
                    }
                });
    }

}
