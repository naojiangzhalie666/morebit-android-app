package com.markermall.cat.login.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseRcView;
import com.markermall.cat.pojo.AreaCodeBean;
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
