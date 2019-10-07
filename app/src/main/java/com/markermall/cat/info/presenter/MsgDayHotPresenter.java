package com.markermall.cat.info.presenter;

import com.markermall.cat.info.contract.MsgDayHotContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.DayHotBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;



public class MsgDayHotPresenter extends MvpPresenter<InfoModel, MsgDayHotContract.View> implements MsgDayHotContract.Present {

    public void getMsg(RxFragment fragment,CommonEmpty emptyView) {
        mModel.getDayHotMsg(fragment)
                .compose(emptyView.<BaseResponse<List<DayHotBean>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onMsgFinally();
                    }
                })
                .subscribe(new DataObserver<List<DayHotBean>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onMsgfailure();
                    }



                    @Override
                    protected void onSuccess(List<DayHotBean> data) {
                        getIView().onMsgSuccessful(data);
                    }
                });
    }



}
