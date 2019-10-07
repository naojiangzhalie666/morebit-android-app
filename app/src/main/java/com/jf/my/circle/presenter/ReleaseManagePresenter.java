package com.jf.my.circle.presenter;

import com.jf.my.circle.contract.ArticleContract;
import com.jf.my.circle.contract.ReleaseManageContract;
import com.jf.my.circle.model.CircleModel;
import com.jf.my.circle.model.ReleaseManageModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CommonEmpty;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.ArticleBody;
import com.jf.my.pojo.ReleaseManage;
import com.jf.my.pojo.SearchArticleBody;
import com.jf.my.pojo.request.RequestReleaseGoodsDelete;
import com.jf.my.pojo.request.RequestReleaseManage;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ReleaseManagePresenter extends MvpPresenter<ReleaseManageModel, ReleaseManageContract.View> implements ReleaseManageContract.Present {




    @Override
    public void getReleaseManageList(RxFragment rxFragment, RequestReleaseManage body, CommonEmpty emptyView) {
        mModel.getReleaseManageList(rxFragment, body)
                .compose(emptyView.<BaseResponse<List<ReleaseManage>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onFinally();
                    }
                })
                .subscribe(new DataObserver<List<ReleaseManage>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onEmpty();
                    }


                    @Override
                    protected void onSuccess(List<ReleaseManage> data) {
                        getIView().onSuccessful(data);
                    }
                });
    }

    @Override
    public void getReleaseGoodsDelete(RxFragment rxFragment, RequestReleaseGoodsDelete body, final int position) {
        mModel.getReleaseGoodsDelete(rxFragment,body)
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        getIView().onNull(position);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        getIView().onGoodsDeleteSuc(data,position);
                    }
                });
    }
}
