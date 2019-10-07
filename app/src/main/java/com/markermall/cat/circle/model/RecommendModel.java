package com.markermall.cat.circle.model;


import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.request.RequestListBody;
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
