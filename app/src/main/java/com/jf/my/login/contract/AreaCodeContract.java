package com.jf.my.login.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseRcView;
import com.jf.my.pojo.AreaCodeBean;
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
