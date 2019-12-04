package com.zjzy.morebit.main.model;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.request.RequestNoticeListBean;
import com.zjzy.morebit.utils.C;

import java.util.List;

import io.reactivex.Observable;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class NoticeModel extends MvpModel {
    public Observable<BaseResponse<List<ImageInfo>>> getNoticeList(BaseActivity activity, int page,int type) {
        RequestNoticeListBean requestBean = new RequestNoticeListBean();
        requestBean.setPage(page);
        requestBean.setOs(C.Setting.os);
        requestBean.setType(type);
        requestBean.setRows(10);
        return RxHttp.getInstance().getSysteService()
                .getNoticeList(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }
}
