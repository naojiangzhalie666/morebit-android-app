package com.zjzy.morebit.info.contract;


import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.EarningsMsg;

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
