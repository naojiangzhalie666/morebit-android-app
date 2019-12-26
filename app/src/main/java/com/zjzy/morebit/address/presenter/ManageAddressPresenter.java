package com.zjzy.morebit.address.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.pojo.address.AddressInfoList;
import com.zjzy.morebit.address.contract.ManageAddressContract;
import com.zjzy.morebit.address.model.ManageAddressModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;

/**
 * Created by haiping.liu on 2019-12-14.
 */
public class ManageAddressPresenter extends MvpPresenter<ManageAddressModel, ManageAddressContract.View>implements ManageAddressContract.Present {

    @Override
    public void deleteAddress(BaseActivity rxActivity, String addressId, final int position) {
        mModel.deleteAdddress(rxActivity,addressId)
                .subscribe(new DataObserver<Boolean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onDeleteError();
                    }

                    @Override
                    protected void onSuccess(Boolean isSuccess) {
                        getIView().onDeleteSuccessful(isSuccess,position);
                    }
                });
    }

    @Override
    public void getAddressList(BaseActivity rxActivity) {
        mModel.getAddressList(rxActivity)
                .subscribe(new DataObserver<AddressInfoList>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onAddressListError();
                    }

                    @Override
                    protected void onSuccess(AddressInfoList list) {
                        getIView().onAddressListSuccessful(list);
                    }
                });


    }

}
