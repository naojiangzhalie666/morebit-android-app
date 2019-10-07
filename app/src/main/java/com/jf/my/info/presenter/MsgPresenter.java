package com.jf.my.info.presenter;

import com.jf.my.info.contract.MsgContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CommonEmpty;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.EarningsMsg;
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
