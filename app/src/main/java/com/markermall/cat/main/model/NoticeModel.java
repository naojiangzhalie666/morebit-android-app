package com.markermall.cat.main.model;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.request.RequestNoticeListBean;
import com.markermall.cat.utils.C;

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
