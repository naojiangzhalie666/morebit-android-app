package com.zjzy.morebit.circle.model;

import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JerryHo on 2019/3/15
 * Description:
 */
public class TutorialModel extends MvpModel {


    /**
     * 获取商学院教程 数据
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getTutorialData(RxFragment fragment, RequestTwoLevel requestTwoLevel) {

        return RxHttp.getInstance().getCommonService().getTutorialData(requestTwoLevel)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }
    /**
     * 获取商学院教程  全部数据
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getArticleList(RxFragment fragment, RequestTwoLevel requestTwoLevel) {

        return RxHttp.getInstance().getCommonService().getArticleList(requestTwoLevel)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }





}
