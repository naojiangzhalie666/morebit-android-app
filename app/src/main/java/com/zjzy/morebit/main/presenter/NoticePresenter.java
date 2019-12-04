package com.zjzy.morebit.main.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.main.contract.NoticeContract;
import com.zjzy.morebit.main.model.NoticeModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.C;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class NoticePresenter extends MvpPresenter<NoticeModel, NoticeContract.View> implements NoticeContract.Present{
    private final int number = 10;
    private int mPage = 1;

    @Override
    public void getNoticeList(BaseActivity activity, final boolean isRefresh, int type) {
        if(isRefresh){
            mPage = 1;
        }else {
            mPage += 1;
        }
        mModel.getNoticeList(activity,mPage,type)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if(!errCode.equals(C.requestCode.dataListEmpty)){
                            getIView().refreshDateFail();
                        }
                    }

                    @Override
                    protected void onDataListEmpty() {
                        getIView().showNoData();
                    }

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        if (isRefresh) {
                            getIView().refreshData(data);
                        } else {
                            getIView().moreData(data);
                        }
                    }
                });
    }
}
