package com.markermall.cat.main.model;

import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.request.RequestSearchStatistics;
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
     * 马克猫热门
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
