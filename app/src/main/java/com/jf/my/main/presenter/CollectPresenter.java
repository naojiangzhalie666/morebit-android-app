package com.jf.my.main.presenter;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.main.contract.CollectContract;
import com.jf.my.main.model.MainModel;
import com.jf.my.main.ui.CollectFragment2;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CommonEmpty;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ShopGoodInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class CollectPresenter extends MvpPresenter<MainModel, CollectContract.View> implements CollectContract.Present {

    @Override
    public void getCollectData(CollectFragment2 fragment, int page, CommonEmpty emptyView) {

        mModel.getCollectData(fragment, page)
                .compose(emptyView.<BaseResponse<List<ShopGoodInfo>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().showCollectFailure(errorMsg,errCode);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        getIView().showCollectSuccess(data);

                    }

                });
    }


    @Override
    public void getDeleteCollection(RxFragment fragment, String ids) {
        mModel.delUserCollection((BaseActivity) fragment.getActivity(), ids)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        getIView().showDeleteSuccess();
                    }
                });
    }


}
