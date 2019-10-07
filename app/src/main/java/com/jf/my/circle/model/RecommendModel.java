package com.jf.my.circle.model;


import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.ArticleBody;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.SearchArticleBody;
import com.jf.my.pojo.StudyRank;
import com.jf.my.pojo.request.RequestListBody;
import com.jf.my.pojo.request.RequestVideoId;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

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
