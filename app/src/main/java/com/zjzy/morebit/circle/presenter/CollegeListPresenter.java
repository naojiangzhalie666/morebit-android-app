package com.zjzy.morebit.circle.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.circle.contract.CollegeListContract;
import com.zjzy.morebit.circle.model.CollegeListModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.requestbodybean.RequestModelId;
import com.zjzy.morebit.pojo.requestbodybean.RequestModelIdData;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by JerryHo on 2019/3/15
 * Description:
 */
public class CollegeListPresenter extends MvpPresenter<CollegeListModel, CollegeListContract.View> implements CollegeListContract.Present {

    @Override
    public void getCollegeListData(BaseActivity baseActivity, RequestModelId requestModelId,int type) {
        mModel.getCollegeListData(baseActivity, requestModelId,type)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onCollegeListFinally();
                    }
                })
                .subscribe(new DataObserver<List<RequestModelIdData>>() {

                    @Override
                    protected void onSuccess(List<RequestModelIdData> data) {
                        getIView().onCollegeListSuccessful(data);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        getIView().onCollegeListEmpty();
                    }
                });
    }


}
