package com.zjzy.morebit.circle.model;


import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class RecommendModel extends MvpModel {


    /**
     * 获取商学院推荐更多文章列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getRecommendMoreList(RxAppCompatActivity fragment, RequestListBody body) {
        return RxHttp.getInstance().getCommonService().getRecommendArticleList(body)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }


    /**
     * 获取商学院预览文章
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getReViewArticleList(RxAppCompatActivity fragment, RequestListBody body) {
        return RxHttp.getInstance().getCommonService().getReViewArticleList(body)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }
}
