package com.zjzy.morebit.circle.model;


import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ArticleBody;
import com.zjzy.morebit.pojo.CollegeHome;
import com.zjzy.morebit.pojo.MarkermallInformation;
import com.zjzy.morebit.pojo.SearchArticleBody;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.request.RequestCircleSearchBean;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.pojo.ImageInfo;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class CircleModel extends MvpModel {

    /**
     * 获取商学院广告轮播图
     *
     * @param fragment
     * @param back
     * @return
     */
    public Observable<BaseResponse<List<ImageInfo>>> getCommercialBanner(RxFragment fragment) {
        return RxHttp.getInstance().getCommonService().getCommercialBanner()
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }

    /**
     * 获取商学院学习等级模块
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<StudyRank>>> getStudyRank(RxFragment fragment) {
        return RxHttp.getInstance().getCommonService().getStudyRank()
                .compose(RxUtils.<BaseResponse<List<StudyRank>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<StudyRank>>>bindToLifecycle());
    }


    /**
     * 获取商学院模块文章列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getArticleList(RxFragment fragment, ArticleBody body) {
        return RxHttp.getInstance().getCommonService().getRecommendMoreList(body)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }

    /**
     * 搜索商学院模块文章列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> searchArticleList(RxFragment fragment, SearchArticleBody body) {
        return RxHttp.getInstance().getCommonService().searchArticleList(body)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }

    /**
     * 获取商学院推荐文章列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getRecommendArticleList(RxFragment fragment, RequestListBody body) {
        return RxHttp.getInstance().getCommonService().getRecommendArticleList(body)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }



    /**
     * 视频点击量更新
     * @param fragment
     * @param id
     * @return
     */
    public Observable<BaseResponse<List<MarkermallInformation>>> getCarousel(RxFragment fragment) {
        return RxHttp.getInstance().getCommonService()
                .getCarousel()
                .compose(RxUtils.<BaseResponse<List<MarkermallInformation>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<MarkermallInformation>>>bindToLifecycle());
    }


    /**
     * 获取商学院首页列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<CollegeHome>>> getPageAggregationList(RxFragment fragment, RequestListBody body) {
        return RxHttp.getInstance().getCommonService().getPageAggregationList(body)
                .compose(RxUtils.<BaseResponse<List<CollegeHome>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<CollegeHome>>>bindToLifecycle());
    }



    /**
     * 商学院搜索热门关键配置
     * @param fragment
     * @param id
     * @return
     */
    public Observable<BaseResponse<List<SearchHotKeyBean>>> getSearchSet(RxAppCompatActivity rxAppCompatActivity, RequestCircleSearchBean bean) {

        return RxHttp.getInstance().getSysteService()
                .getSearchSet(bean)
                .compose(RxUtils.<BaseResponse<List<SearchHotKeyBean>>>switchSchedulers())
                .compose(rxAppCompatActivity.<BaseResponse<List<SearchHotKeyBean>>>bindToLifecycle());
    }
}
