package com.zjzy.morebit.info.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.EarningsMsg;

import java.util.List;

/**
 * 收货地址的view
 * Created by haiping.liu on 2019-12-05.
 */
public class AddressContract {
    public interface View extends BaseView {
        void onAddressSuccessful(List<EarningsMsg> data);
        void onAddressfailure();
        void onAddressFinally();
    }

    public interface Present extends BasePresenter {

    }
}
