package com.markermall.cat.info.presenter;

import com.markermall.cat.info.contract.MsgContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.EarningsMsg;
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
