package com.zjzy.morebit.address.presenter;

import com.smarttop.library.bean.AdressBean;
import com.smarttop.library.db.manager.AddressDictManager;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.pojo.address.AddressInfo;
import com.zjzy.morebit.pojo.address.AllRegionInfoList;
import com.zjzy.morebit.pojo.address.RegionInfo;
import com.zjzy.morebit.address.contract.AddOrModifyAddressContract;
import com.zjzy.morebit.address.model.AddOrModifyAddressModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiping.liu on 2019-12-14.
 */
public class AddOrModifyAddressPresenter extends MvpPresenter<AddOrModifyAddressModel,AddOrModifyAddressContract.View>implements AddOrModifyAddressContract.Present {
    /**
     * 添加地址
     * @param rxActivity
     * @param info
     */
    @Override
    public void addAddress(BaseActivity rxActivity, AddressInfo info) {
        mModel.addAdddress(rxActivity,info)
                .subscribe(new DataObserver<Boolean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onAddError();
                    }

                    @Override
                    protected void onSuccess(Boolean isSuccess) {
                        getIView().onAddSuccessful(isSuccess);
                    }
                });
    }

    /**
     * 更新地址
     * @param rxActivity
     * @param info
     */
    @Override
    public void updateAddress(BaseActivity rxActivity, AddressInfo info) {
        mModel.updateAddress(rxActivity,info)
                .subscribe(new DataObserver<Boolean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onUpdateError();
                    }

                    @Override
                    protected void onSuccess(Boolean isSuccess) {
                        getIView().onUpdateSuccessful(isSuccess);
                    }
                });
    }

    @Override
    public void getAllRegion(final BaseActivity rxActivity) {
        mModel.getAllRegionInfoList(rxActivity)
                .compose(RxUtils.<BaseResponse<AllRegionInfoList>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<AllRegionInfoList>>bindToLifecycle())
                .subscribe(new DataObserver<AllRegionInfoList>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        //拉取失败
                        MyLog.e("mainlhp","error："+errCode+"msg："+errorMsg);
                        UserLocalData.setSavedRegionFlag(false);
                    }

                    @Override
                    protected void onSuccess(AllRegionInfoList data) {
//                        checkGrayUpgrade(data,appUpgradeInfo,rxActivity);
//                        UserLocalData.setSavedRegionFlag(true);
                        MyLog.e("mainlhp","请求成功-allRegions");
                        if (data == null){
                            return;
                        }
                        int size = data.getList().size();
                        if (size == 0){
                            return;
                        }
                        List<RegionInfo> list = data.getList();
                        List<AdressBean.ChangeRecordsBean> addresslist = new ArrayList<>();
                        for (int i = 0;i <size;i++ ){
                            RegionInfo info = list.get(i);
                            AdressBean.ChangeRecordsBean bean = new AdressBean.ChangeRecordsBean();
                            bean.id = info.getId();
                            bean.code = String.valueOf(info.getCode());
                            bean.parentId = info.getPid();
                            bean.name = info.getName();
                            addresslist.add(bean);
                            MyLog.d("mainlhp","id:"+bean.id+"code:"+bean.code+"parentId:"+bean.parentId+"name:"+bean.name);
                        }
                        AddressDictManager addressDictManager = new AddressDictManager(rxActivity);


                        addressDictManager.deleteAll();
                        MyLog.e("mainlhp","开始拉取地区数据");
                        addressDictManager.insertAddress(addresslist);
                        UserLocalData.setSavedRegionFlag(true);
                        MyLog.e("mainlhp","结束拉取地区数据");
                    }
                });
    }
}
