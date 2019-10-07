package com.jf.my.info.presenter;

import com.jf.my.info.contract.MsgDayHotContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CommonEmpty;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.DayHotBean;
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
