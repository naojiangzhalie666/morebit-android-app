package com.jf.my.circle.presenter;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.circle.contract.CommercialCollegeContract;
import com.jf.my.circle.contract.RecommendContract;
import com.jf.my.circle.model.CircleModel;
import com.jf.my.circle.model.RecommendModel;
import com.jf.my.circle.ui.RecommendListActivity;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CommonEmpty;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.ReleaseManage;
import com.jf.my.pojo.StudyRank;
import com.jf.my.pojo.request.RequestBannerBean;
import com.jf.my.pojo.request.RequestListBody;
import com.jf.my.pojo.requestbodybean.RequestModelId;
import com.jf.my.utils.C;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

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
