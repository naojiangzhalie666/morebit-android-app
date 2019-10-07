package com.markermall.cat.circle.model;


import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.ReleaseManage;
import com.markermall.cat.pojo.request.RequestReleaseGoodsDelete;
import com.markermall.cat.pojo.request.RequestReleaseManage;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class ReleaseManageModel extends MvpModel {
    /**
     * 获取商品发布管理列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<ReleaseManage>>> getReleaseManageList(RxFragment fragment, RequestReleaseManage requestReleaseManage) {
        return RxHttp.getInstance().getCommonService().getReleaseManageList(requestReleaseManage)
                .compose(RxUtils.<BaseResponse<List<ReleaseManage>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ReleaseManage>>>bindToLifecycle());
    }

    /**
     * 发布的商品删除
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<String>> getReleaseGoodsDelete(RxFragment fragment, RequestReleaseGoodsDelete requestReleaseGoodsDelete) {
        return RxHttp.getInstance().getCommonService().getReleaseGoodsDelete(requestReleaseGoodsDelete)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());
    }
}
