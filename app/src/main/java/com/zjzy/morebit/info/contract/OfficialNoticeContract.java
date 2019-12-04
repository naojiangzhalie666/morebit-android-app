package com.zjzy.morebit.info.contract;


import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseRcView;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class OfficialNoticeContract {
    public interface View extends BaseRcView {
        void showData(List<ProtocolRuleBean> officialNotices, int type);
        void showFailureMessage( );
    }

    public interface Present extends BasePresenter {
        void getOfficialNotice(RxFragment baseFragment,int type);
    }
}
