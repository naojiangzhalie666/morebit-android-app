package com.zjzy.morebit.address.contract;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.address.AddressInfoList;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;

import java.util.List;

/**
 * 管理地址页面
 * Created by haiping.liu on 2019-12-14.
 */
public class ManageAddressContract {

    public interface View extends BaseView {

        void onDeleteError();

        void onDeleteSuccessful(Boolean isSuccess,int position);

        void onAddressListError();

        void onAddressListSuccessful(AddressInfoList list);

    }

    public interface Present extends BasePresenter {
        /**
         * 删除地址
         * @param rxActivity
         * @param AddressId
         */
        void deleteAddress(BaseActivity rxActivity, String AddressId,int position);

        /**
         * 编辑地址
         * @param rxActivity
         */
        void getAddressList(BaseActivity rxActivity);


    }
}
