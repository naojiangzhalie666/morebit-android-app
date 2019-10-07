package com.markermall.cat.main.presenter;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.main.contract.NoticeContract;
import com.markermall.cat.main.model.NoticeModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.utils.C;

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
