package com.jf.my.main.model;

import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.pojo.request.RequestSearchStatistics;
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
     * 蜜源热门
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
