package com.zjzy.morebit.circle.model;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.circle.CollegeListActivity;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.requestbodybean.RequestModelId;
import com.zjzy.morebit.pojo.requestbodybean.RequestModelIdData;

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
