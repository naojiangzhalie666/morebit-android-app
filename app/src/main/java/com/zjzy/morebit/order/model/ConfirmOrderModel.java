package com.zjzy.morebit.order.model;


import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.order.ResponseOrderInfo;
import com.zjzy.morebit.pojo.request.RequestCreateOrderBean;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/12/15.
 * 会员商品详情 model
 */
public class ConfirmOrderModel extends MvpModel {
    /**
     * 获取用户默认收货地址
     * @param rxActivity
     * @return
     */
    public Observable<BaseResponse<AddressInfo>> getdefaultAddress(BaseActivity rxActivity) {
        return RxHttp.getInstance().getCommonService().getDefaultAddresses()
                .compose(RxUtils.<BaseResponse<AddressInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<AddressInfo>>bindToLifecycle());
    }

    /**
     * 创建会员商品订单
     * @param rxActivity
     * @param addressId
     * @param goodsId
     * @param goodsNum
     * @param goodsPrice
     * @param totalPrice
     * @return
     */
    public Observable<BaseResponse<ResponseOrderInfo>> createOrderForVip(BaseActivity rxActivity,int addressId,
                                                            int goodsId,
                                                            int goodsNum,
                                                            String goodsPrice,
                                                            String totalPrice
    ) {
        RequestCreateOrderBean requestBean = new RequestCreateOrderBean();
        requestBean.setAddressId(addressId);
        requestBean.setGoodsId(goodsId);
        requestBean.setGoodsNum(goodsNum);
        requestBean.setGoodsPrice(goodsPrice);
        requestBean.setTotalPrice(totalPrice);

        return RxHttp.getInstance().getCommonService().createOrderForVip(requestBean)
                .compose(RxUtils.<BaseResponse<ResponseOrderInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ResponseOrderInfo>>bindToLifecycle());
    }




}