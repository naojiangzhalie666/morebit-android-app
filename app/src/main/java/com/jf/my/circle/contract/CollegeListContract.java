package com.jf.my.circle.contract;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.requestbodybean.RequestModelId;
import com.jf.my.pojo.requestbodybean.RequestModelIdData;

import java.util.List;

/**
 * Created by JerryHo on 2019/3/16
 * Description: 商学院activity 获取tab等数据
 */
public class CollegeListContract {

    public interface View extends BaseView {

        void onCollegeListSuccessful(List<RequestModelIdData> data);

        void onCollegeListEmpty();

        void onCollegeListFinally();
    }

    public interface Present extends BasePresenter {

        void getCollegeListData(BaseActivity baseActivity, RequestModelId requestModelId,int type);
    }

}
