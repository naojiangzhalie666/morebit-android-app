package com.zjzy.morebit.address.model;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.pojo.address.AddressInfoList;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.request.RequestDeleteAddressIdBean;

import io.reactivex.Observable;

/**
 * Created by haiping.liu on 2019-12-14.
 */
public class ManageAddressModel extends MvpModel {
    /**
     * 删除地址
     *
     * @param rxActivity
     * @param addressId
     * @return
     */
    public Observable<BaseResponse<Boolean>> deleteAdddress(BaseActivity rxActivity, String addressId) {
        RequestDeleteAddressIdBean bean = new RequestDeleteAddressIdBean();
        bean.setId(addressId);
        return RxHttp.getInstance().getGoodsService().deleteAddress(bean)
                .compose(RxUtils.<BaseResponse<Boolean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Boolean>>bindToLifecycle());
    }
    /**
     *
     * 返回收货地址列表
     * @param rxActivity
     * @return
     */
    public Observable<BaseResponse<AddressInfoList>> getAddressList(BaseActivity rxActivity) {

        return RxHttp.getInstance().getGoodsService().getAddressList()
                .compose(RxUtils.<BaseResponse<AddressInfoList>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<AddressInfoList>>bindToLifecycle());
    }


}
