package com.zjzy.morebit.goods.shopping.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.TeamData;

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
