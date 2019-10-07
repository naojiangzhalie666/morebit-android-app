package com.markermall.cat.circle.model;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.circle.CollegeListActivity;
import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.requestbodybean.RequestModelId;
import com.markermall.cat.pojo.requestbodybean.RequestModelIdData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JerryHo on 2019/3/16
 * Description:
 */
public class CollegeListModel extends MvpModel {

    /**
     * 获取商学院二级tab数据
     *
     * @param baseActivity
     * @param requestModelId
     * @return
     */
    public Observable<BaseResponse<List<RequestModelIdData>>> getCollegeListData(BaseActivity baseActivity, RequestModelId requestModelId, int type) {
        if (type == CollegeListActivity.NEW_COURSE) {
            return RxHttp.getInstance().getCommonService().getNewCollegeListData()
                    .compose(RxUtils.<BaseResponse<List<RequestModelIdData>>>switchSchedulers())
                    .compose(baseActivity.<BaseResponse<List<RequestModelIdData>>>bindToLifecycle());
        } else {
            return RxHttp.getInstance().getCommonService().getCollegeListData(requestModelId)
                    .compose(RxUtils.<BaseResponse<List<RequestModelIdData>>>switchSchedulers())
                    .compose(baseActivity.<BaseResponse<List<RequestModelIdData>>>bindToLifecycle());
        }
    }
}
