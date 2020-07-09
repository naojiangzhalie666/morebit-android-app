package com.zjzy.morebit.info.presenter;

import android.util.Log;

import com.zjzy.morebit.info.contract.MsgDayHotContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.DayHotBean;
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


    public void getNoticede(RxFragment fragment,int  type){
        mModel.getReadNotice(fragment,type)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onSuccess(String data) {

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        Log.e("sssss","2");
                    }
                });
    }



}
