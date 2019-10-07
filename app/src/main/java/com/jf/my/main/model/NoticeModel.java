package com.jf.my.main.model;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.request.RequestNoticeListBean;
import com.jf.my.utils.C;

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
