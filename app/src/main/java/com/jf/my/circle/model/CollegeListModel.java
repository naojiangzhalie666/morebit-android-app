package com.jf.my.circle.model;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.circle.CollegeListActivity;
import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.pojo.requestbodybean.RequestModelId;
import com.jf.my.pojo.requestbodybean.RequestModelIdData;

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
