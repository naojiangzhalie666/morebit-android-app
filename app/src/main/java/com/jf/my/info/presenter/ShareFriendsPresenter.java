package com.jf.my.info.presenter;

import com.jf.my.info.contract.EarningsContract;
import com.jf.my.info.contract.ShareFriendsContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ImageInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

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
