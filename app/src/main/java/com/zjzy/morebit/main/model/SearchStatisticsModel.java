package com.zjzy.morebit.main.model;

import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.request.RequestSearchStatistics;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;

/**
 * @Author: wuchaowen
 * @Description: 搜索统计
 * @Time:
 **/
public class SearchStatisticsModel extends MvpModel {
    private static SearchStatisticsModel searchStatisticsModel;

    public static SearchStatisticsModel getInstance() {
        if (searchStatisticsModel == null)
            searchStatisticsModel = new SearchStatisticsModel();
        return searchStatisticsModel;
    }

    public Observable<BaseResponse<String>> sendSearchStatistics(RxAppCompatActivity activity, String keywords,String id,String type) {

        RequestSearchStatistics requestBean = new RequestSearchStatistics();
        requestBean.setKeyWords(keywords);
        requestBean.setAdId(id);
        requestBean.setType(type);
        return RxHttp.getInstance().getCommonService().searchStatistics(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers());
    }

    /**
     * 多点优选热门
     * @param activity
     * @param id
     * @param type
     * @return
     */
    public Observable<BaseResponse<String>> sendHotStatistics(RxAppCompatActivity activity,String id,String type) {

        RequestSearchStatistics requestBean = new RequestSearchStatistics();
        requestBean.setAdId(id);
        requestBean.setType(type);
        return RxHttp.getInstance().getCommonService().clickStatistics(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers());
    }
}
