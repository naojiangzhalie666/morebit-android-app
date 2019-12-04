package com.zjzy.morebit.login.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseRcView;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;


public class AreaCodeContract {
    public interface View extends BaseRcView {
        void updateAreaCode(List<AreaCodeBean> data);
    }

    public interface Present extends BasePresenter {
        void getCountryList(RxAppCompatActivity activity);

    }
}
