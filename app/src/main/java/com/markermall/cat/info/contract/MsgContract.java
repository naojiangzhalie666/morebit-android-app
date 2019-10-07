package com.markermall.cat.info.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.EarningsMsg;

import java.util.List;

/**
 * Created by fengrs on 2018/9/12.
 */

public class MsgContract {
    public interface View extends BaseView {
        void onMsgSuccessful(List<EarningsMsg> data);
        void onMsgfailure();
        void onMsgFinally();
    }

    public interface Present extends BasePresenter {

    }
}
