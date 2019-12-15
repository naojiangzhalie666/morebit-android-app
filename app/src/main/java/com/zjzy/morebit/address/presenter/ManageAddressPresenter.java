package com.zjzy.morebit.address.presenter;

import com.smarttop.library.db.AssetsDatabaseManager;
import com.smarttop.library.db.manager.AddressDictManager;
import com.smarttop.library.widget.AddressSelector;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.address.AddressInfoList;
import com.zjzy.morebit.address.AllRegionInfoList;
import com.zjzy.morebit.address.contract.AddOrModifyAddressContract;
import com.zjzy.morebit.address.contract.ManageAddressContract;
import com.zjzy.morebit.address.model.AddOrModifyAddressModel;
import com.zjzy.morebit.address.model.ManageAddressModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.utils.MyLog;

import java.util.List;

/**
 * Created by haiping.liu on 2019-12-14.
 */
public class ManageAddressPresenter extends MvpPresenter<ManageAddressModel, ManageAddressContract.View>implements ManageAddressContract.Present {

    @Override
    public void deleteAddress(BaseActivity rxActivity, String addressId) {
        mModel.deleteAdddress(rxActivity,addressId)
                .subscribe(new DataObserver<Boolean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onDeleteError();
                    }

                    @Override
                    protected void onSuccess(Boolean isSuccess) {
                        getIView().onDeleteSuccessful(isSuccess);
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
