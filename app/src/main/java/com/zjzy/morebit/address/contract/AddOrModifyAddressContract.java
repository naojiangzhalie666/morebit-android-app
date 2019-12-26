package com.zjzy.morebit.address.contract;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.pojo.address.AddressInfo;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;

/**
 *
 * Created by haiping.liu on 2019-12-14.
 */
public class AddOrModifyAddressContract {

    public interface View extends BaseView {

        void onAddError();

        void onAddSuccessful(Boolean isSuccess);

        void onUpdateError();

        void onUpdateSuccessful(Boolean isSuccess);

    }

    public interface Present extends BasePresenter {
        /**
         * 添加地址
         * @param rxActivity
         * @param info
         */
        void addAddress(BaseActivity rxActivity, AddressInfo info);

        /**
         * 编辑地址
         * @param rxActivity
         * @param info
         */
        void updateAddress(BaseActivity rxActivity, AddressInfo info);

        void getAllRegion(BaseActivity rxActivity);
    }
}
