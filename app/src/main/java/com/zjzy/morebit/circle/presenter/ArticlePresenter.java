package com.zjzy.morebit.circle.presenter;

import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ArticleBody;
import com.zjzy.morebit.pojo.SearchArticleBody;
import com.zjzy.morebit.circle.contract.ArticleContract;
import com.zjzy.morebit.circle.model.CircleModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.observer.DataObserver;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ArticlePresenter extends MvpPresenter<CircleModel, ArticleContract.View> implements ArticleContract.Present {

    @Override
    public void getArticleList(RxFragment rxFragment, ArticleBody body,CommonEmpty emptyView) {
            mModel.getArticleList(rxFragment, body)
                    .compose(emptyView.<BaseResponse<List<Article>>>bind())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getIView().onArticleFinally();
                        }
                    })
                    .subscribe(new DataObserver<List<Article>>() {
                        @Override
                        protected void onDataListEmpty() {
                            getIView().onArticleEmpty();
                        }


                        @Override
                        protected void onSuccess(List<Article> data) {
                            getIView().onArticleSuccessful(data);
                        }
                    });
    }




    @Override
    public void searchArticleList(RxFragment rxFragment, SearchArticleBody body, CommonEmpty emptyView) {
        mModel.searchArticleList(rxFragment, body)
                .compose(emptyView.<BaseResponse<List<Article>>>bind())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onArticleFinally();
                    }
                })
                .subscribe(new DataObserver<List<Article>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onArticleEmpty();
                    }


                    @Override
                    protected void onSuccess(List<Article> data) {
                        getIView().onArticleSuccessful(data);
                    }
                });
    }

}
