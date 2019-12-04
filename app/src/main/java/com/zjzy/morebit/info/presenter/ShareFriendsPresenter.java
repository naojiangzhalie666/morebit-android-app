package com.zjzy.morebit.info.presenter;

import com.zjzy.morebit.info.contract.ShareFriendsContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ShareFriendsPresenter extends MvpPresenter<InfoModel, ShareFriendsContract.View> implements ShareFriendsContract.Present {

    @Override
    public void getBanner(RxFragment fragment, int back) {
        mModel.getBanner(fragment,back)
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        getIView().onSuccessful(data);
                    }
                });
    }
}
