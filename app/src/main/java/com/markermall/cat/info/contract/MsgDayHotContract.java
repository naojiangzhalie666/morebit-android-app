package com.markermall.cat.info.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.DayHotBean;

import java.util.List;

/**
 * Created by fengrs on 2018/9/12.
 */

public class MsgDayHotContract {
    public interface View extends BaseView {
        void onMsgSuccessful(List<DayHotBean> data);
        void onMsgfailure();
        void onMsgFinally();
    }

    public interface Present extends BasePresenter {

    }
}
