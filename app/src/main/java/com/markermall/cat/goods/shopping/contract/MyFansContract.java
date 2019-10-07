package com.markermall.cat.goods.shopping.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.TeamData;

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
