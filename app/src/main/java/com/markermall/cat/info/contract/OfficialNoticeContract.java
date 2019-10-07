package com.markermall.cat.info.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseRcView;
import com.markermall.cat.pojo.ProtocolRuleBean;
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
