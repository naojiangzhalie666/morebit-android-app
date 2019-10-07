package com.jf.my.goods.shopping.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.TeamData;

/**
 * Created by YangBoTian on 2018/8/20.
 */

public class MyFansContract  {
    public interface View extends BaseView {
        void successful(TeamData data);
        void failure();
        void complete();
    }

    public interface Present extends BasePresenter {

    }
}
