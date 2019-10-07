package com.markermall.cat.circle.presenter;

import com.markermall.cat.circle.contract.RecommendContract;
import com.markermall.cat.circle.model.RecommendModel;
import com.markermall.cat.circle.ui.RecommendListActivity;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.request.RequestListBody;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class RecommendPresenter extends MvpPresenter<RecommendModel, RecommendContract.View> implements RecommendContract.Present {




    @Override
    public void getRecommendMoreList(RxAppCompatActivity baseActivity, RequestListBody body, CommonEmpty emptyView,int type) {
       if(type == RecommendListActivity.ARTICLE_RECOMMEND){
           mModel.getRecommendMoreList(baseActivity, body)
                   .compose(emptyView.<BaseResponse<List<Article>>>bind())
                   .doFinally(new Action() {
                       @Override
                       public void run() throws Exception {
                           getIView().onListFinally();
                       }
                   })
                   .subscribe(new DataObserver<List<Article>>(false) {
                       @Override
                       protected void onDataListEmpty() {
                           getIView().onListEmpty();
                       }


                       @Override
                       protected void onSuccess(List<Article> data) {
                           getIView().onListSuccessful(data);
                       }
                   });
       } else {
           mModel.getReViewArticleList(baseActivity, body)
                   .compose(emptyView.<BaseResponse<List<Article>>>bind())
                   .doFinally(new Action() {
                       @Override
                       public void run() throws Exception {
                           getIView().onListFinally();
                       }
                   })
                   .subscribe(new DataObserver<List<Article>>(false) {
                       @Override
                       protected void onDataListEmpty() {
                           getIView().onListEmpty();
                       }


                       @Override
                       protected void onSuccess(List<Article> data) {
                           getIView().onListSuccessful(data);
                       }
                   });
       }

    }
}
