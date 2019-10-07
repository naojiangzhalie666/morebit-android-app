package com.markermall.cat.info.presenter;

import com.markermall.cat.info.contract.ShareFriendsContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.ImageInfo;
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
