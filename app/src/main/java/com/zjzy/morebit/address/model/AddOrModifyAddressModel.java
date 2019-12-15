package com.zjzy.morebit.address.model;


import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.address.AllRegionInfoList;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.request.RequestAddAddressBean;
import com.zjzy.morebit.pojo.request.RequestUpdateAddressBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestPage;

import java.util.List;

import io.reactivex.Observable;

/**
 * 新增或者修改地址模式
 * Created by haiping.liu on 2019-12-11.
 */
public class AddOrModifyAddressModel extends MvpModel {
    /**
     * 新增地址
     *
     * @param rxActivity
     * @param info
     * @return
     */
    public Observable<BaseResponse<Boolean>> addAdddress(BaseActivity rxActivity, AddressInfo info) {
        RequestAddAddressBean bean = new RequestAddAddressBean();
        bean.setName(info.getName());
        bean.setCity(info.getCity());
        bean.setDefault(info.isDefault());
        bean.setDetailAddress(info.getDetailAddress());
        bean.setDistrict(info.getDistrict());
        bean.setProvince(info.getProvince());
        bean.setTel(info.getTel());

        return RxHttp.getInstance().getGoodsService().addAddress(bean)
                .compose(RxUtils.<BaseResponse<Boolean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Boolean>>bindToLifecycle());
    }
    /**
     * 编辑地址
     */
    public Observable<BaseResponse<Boolean>> updateAddress(BaseActivity rxActivity, AddressInfo info) {
        RequestUpdateAddressBean bean = new RequestUpdateAddressBean();
        bean.setName(info.getName());
        bean.setCity(info.getCity());
        bean.setDefault(info.isDefault());
        bean.setDetailAddress(info.getDetailAddress());
        bean.setDistrict(info.getDistrict());
        bean.setProvince(info.getProvince());
        bean.setTel(info.getTel());
        bean.setId(info.getId());
        return RxHttp.getInstance().getGoodsService().updateAddress(bean)
                .compose(RxUtils.<BaseResponse<Boolean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Boolean>>bindToLifecycle());
    }

    /**
     * 获取所有区域内容
     *
     * @return
     */
    public Observable<BaseResponse<AllRegionInfoList>> getAllRegionInfoList(RxAppCompatActivity rxActivity) {
        return RxHttp.getInstance().getUsersService().getAllRegions()
                .compose(RxUtils.<BaseResponse<AllRegionInfoList>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<AllRegionInfoList>>bindToLifecycle());
    }

}
