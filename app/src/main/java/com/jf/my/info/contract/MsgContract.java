package com.jf.my.info.contract;


import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.EarningsMsg;

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
